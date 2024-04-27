package com.clear.solutions.user;

import static com.clear.solutions.testspecific.hamcrest.TemporalStringMatchers.instantThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clear.solutions.payload.user.UserCreateRequest;
import com.clear.solutions.payload.user.UserPartialUpdateRequest;
import com.clear.solutions.payload.user.UserUpdateRequest;
import com.clear.solutions.testspecific.ControllerLevelIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ControllerLevelIntegrationTest
class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Transactional
  void shouldReturnUserResponseWhenCreatingUser() throws Exception {

    var request = new UserCreateRequest("johndoe2@gmail.com", "John", "Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567480");

    // when
    mockMvc.perform(post("/users").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue(), Long.class))
        .andExpect(jsonPath("$.email", is(request.getEmail())))
        .andExpect(jsonPath("$.firstName", is(request.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(request.getLastName())))
        .andExpect(jsonPath("$.birthDate", is(request.getBirthDate().toString())))
        .andExpect(jsonPath("$.address", is(request.getAddress())))
        .andExpect(jsonPath("$.phoneNumber", is(request.getPhoneNumber())))
        .andExpect(jsonPath("$.createdAt", instantThat(lessThan(Instant.now()))))
        .andExpect(jsonPath("$.lastModifiedAt", instantThat(lessThan(Instant.now()))))
        .andExpect(jsonPath("$._links.self.href", notNullValue()));
  }

  @Test
  @Transactional
  void shouldReturnErrorResponseWhenCreatingUserWithExistingEmail() throws Exception {
    // given
    var request = new UserCreateRequest("johndoe@gmail.com", "John", "Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890");

    // when
    mockMvc.perform(post("/users").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isConflict())
        .andExpect(jsonPath("$.status", is(409)))
        .andExpect(jsonPath("$.title", is("User already exists"))).andExpect(
            jsonPath("$.detail", is("User with email " + request.getEmail() + " already exists")))
        .andExpect(jsonPath("$.instance", is("/users")));
  }


  @Test
  void shouldReturnUserResponseWhenGetUserById() throws Exception {

    mockMvc.perform(get("/users/1").contentType("application/json")).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1L), Long.class))
        .andExpect(jsonPath("$.email", is("johndoe@gmail.com")))
        .andExpect(jsonPath("$.firstName", is("John"))).andExpect(jsonPath("$.lastName", is("Doe")))
        .andExpect(jsonPath("$.birthDate", is("2002-02-22")))
        .andExpect(jsonPath("$.phoneNumber", is("+1234567892")))
        .andExpect(jsonPath("$.createdAt", instantThat(lessThan(Instant.now()))))
        .andExpect(jsonPath("$.lastModifiedAt", instantThat(lessThan(Instant.now()))))
        .andExpect(jsonPath("$._links.self.href", is("/users/1")));
  }

  @Test
  void shouldReturnErrorResponseWhenGetUserByIdAndNoSuchUserByIdException() throws Exception {
    // when
    mockMvc.perform(get("/users/404").contentType("application/json"))
        .andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.title", is("No such user")))
        .andExpect(jsonPath("$.detail", is("User with id 404 not found")))
        .andExpect(jsonPath("$.instance", is("/users/404")));
  }

  @Test
  @Transactional
  void shouldReturnUserResponseWhenUpdateSomeUserFields() throws Exception {
    var instantBeforeUpdate = Instant.now();
    var request = new UserPartialUpdateRequest(null, null, null, LocalDate.parse("2004-02-22"),
        "New address", null);

    mockMvc.perform(patch("/users/1").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
        .andExpect(jsonPath("$.birthDate", is(request.getBirthDate().toString())))
        .andExpect(jsonPath("$.address", is(request.getAddress())))
        .andExpect(jsonPath("$.createdAt", instantThat(lessThan(instantBeforeUpdate))))
        .andExpect(jsonPath("$.lastModifiedAt", instantThat(greaterThan(instantBeforeUpdate))))
        .andExpect(jsonPath("$._links.self.href", is("/users/1")));
  }

  @Test
  @Transactional
  void shouldReturnErrorResponseWhenUpdateSomeUserFieldsAndNoSuchUserByIdException()
      throws Exception {
    // given
    var request = new UserPartialUpdateRequest(null, null, null, null, "New address", null);

    // when
    mockMvc.perform(patch("/users/404").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404))).andExpect(jsonPath("$.title", is("No such user")))
        .andExpect(jsonPath("$.detail", is("User with id 404 not found")))
        .andExpect(jsonPath("$.instance", is("/users/404")));
  }

  @Test
  @Transactional
  void shouldReturnUserResponseWhenUpdateAllUserFields() throws Exception {
    var request = new UserUpdateRequest("newemail@gmail.com", "New John", "New Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890");

    mockMvc.perform(put("/users/1").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1L), Long.class))
        .andExpect(jsonPath("$.email", is(request.getEmail())))
        .andExpect(jsonPath("$.firstName", is(request.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(request.getLastName())))
        .andExpect(jsonPath("$.birthDate", is(request.getBirthDate().toString())))
        .andExpect(jsonPath("$.address", is(request.getAddress())))
        .andExpect(jsonPath("$.phoneNumber", is(request.getPhoneNumber())))
        .andExpect(jsonPath("$._links.self.href", is("/users/1")));
  }

  @Test
  @Transactional
  void shouldReturnErrorResponseWhenUpdateAllUserFieldsAndNoSuchUserByIdException()
      throws Exception {
    // given
    var request = new UserUpdateRequest("newemail@gmail.com", "New John", "New Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890");
    // when
    mockMvc.perform(put("/users/404").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404))).andExpect(jsonPath("$.title", is("No such user")))
        .andExpect(jsonPath("$.detail", is("User with id 404 not found")))
        .andExpect(jsonPath("$.instance", is("/users/404")));
  }

  @Test
  @Transactional
  void shouldReturnNoContentWhenDeleteUser() throws Exception {
    mockMvc.perform(delete("/users/1").contentType("application/json"))
        .andExpect(status().isNoContent());
  }

  @Test
  @Transactional
  void shouldReturnNotFoundWhenDeleteNonExistingUser() throws Exception {

    mockMvc.perform(delete("/users/404").contentType("application/json"))
        .andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.title", is("No such user")))
        .andExpect(jsonPath("$.detail", is("User with id 404 not found")))
        .andExpect(jsonPath("$.instance", is("/users/404")));
  }

  @Test
  void shouldReturnListOfUsersByBirthDateRange() throws Exception {
    mockMvc.perform(get("/users").contentType("application/json").param("from", "1990-03-03")
            .param("to", "2005-05-20")).andExpect(status().isOk())
        .andExpect(jsonPath("$.page.size", is(20)))
        .andExpect(jsonPath("$.page.totalElements", greaterThan(0)));
  }
}
