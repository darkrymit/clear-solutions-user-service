package com.clear.solutions.user;

import static com.clear.solutions.testspecific.hamcrest.TemporalStringMatchers.instantComparesEqualTo;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clear.solutions.payload.range.RangeLocalDateCriteria;
import com.clear.solutions.payload.user.UserCreateRequest;
import com.clear.solutions.payload.user.UserPartialUpdateRequest;
import com.clear.solutions.payload.user.UserResponse;
import com.clear.solutions.payload.user.UserUpdateRequest;
import com.clear.solutions.testspecific.ControllerLevelUnitTest;
import com.clear.solutions.user.exception.NoSuchUserByIdException;
import com.clear.solutions.user.exception.UserAlreadyExistsByEmailException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.test.web.servlet.MockMvc;

@ControllerLevelUnitTest(controllers = UserController.class)
class UserControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  @Test
  void shouldReturnUserResponseWhenCreatingUser() throws Exception {
    // given
    var timeOfCreationAndModification = Instant.now();
    var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890", timeOfCreationAndModification,
        timeOfCreationAndModification);
    response.add(Link.of("/users/1", "self"));
    response.add(Link.of("/users/1", "update-all-fields"));
    response.add(Link.of("/users/1", "update-some-fields"));
    response.add(Link.of("/users/1", "delete"));

    var request = new UserCreateRequest("johndoe@gmail.com", "John", "Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890");

    when(userService.create(request)).thenReturn(response);

    // when
    mockMvc.perform(post("/users").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
        .andExpect(jsonPath("$.email", is(response.getEmail())))
        .andExpect(jsonPath("$.firstName", is(response.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(response.getLastName())))
        .andExpect(jsonPath("$.birthDate", is(response.getBirthDate().toString())))
        .andExpect(jsonPath("$.address", is(response.getAddress())))
        .andExpect(jsonPath("$.phoneNumber", is(response.getPhoneNumber())))
        .andExpect(jsonPath("$.createdAt", instantComparesEqualTo(response.getCreatedAt())))
        .andExpect(
            jsonPath("$.lastModifiedAt", instantComparesEqualTo(response.getLastModifiedAt())))
        .andExpect(jsonPath("$._links.self.href", is("/users/1")))
        .andExpect(jsonPath("$._links.update-all-fields.href", is("/users/1")))
        .andExpect(jsonPath("$._links.update-some-fields.href", is("/users/1")))
        .andExpect(jsonPath("$._links.delete.href", is("/users/1")));
  }

  @Test
  void shouldErrorResponseWhenCreatingUserWithInvalidAge() throws Exception {
    // given
    var request = new UserCreateRequest("johndoe@gmail.com", "John", "Doe",
        LocalDate.of(2008, 5, 20), "New address", "+1234567890");

    mockMvc.perform(post("/users").contentType("application/json").content(
            objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.title", is("Bad Request")))
        .andExpect(jsonPath("$.instance", is("/users")))
        .andExpect(jsonPath("$.detail", containsString("Validation failed")))
        .andExpect(jsonPath("$.errors[0].object", is("userCreateRequest")))
        .andExpect(jsonPath("$.errors[0].field", is("birthDate")))
        .andExpect(jsonPath("$.errors[0].message", containsString("18")));
  }

  @Test
  void shouldReturnErrorResponseWhenCreatingUserWithExistingEmail() throws Exception {
    // given
    var request = new UserCreateRequest("johndoe@gmail.com", "John", "Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890");

    when(userService.create(request)).thenThrow(
        new UserAlreadyExistsByEmailException(request.getEmail()));

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
    // given
    var timeOfCreationAndModification = Instant.now();
    var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890", timeOfCreationAndModification,
        timeOfCreationAndModification);
    response.add(Link.of("/users/1", "self"));
    response.add(Link.of("/users/1", "update-all-fields"));
    response.add(Link.of("/users/1", "update-some-fields"));
    response.add(Link.of("/users/1", "delete"));

    when(userService.getById(1L)).thenReturn(response);
    mockMvc.perform(get("/users/1").contentType("application/json")).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
        .andExpect(jsonPath("$.email", is(response.getEmail())))
        .andExpect(jsonPath("$.firstName", is(response.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(response.getLastName())))
        .andExpect(jsonPath("$.birthDate", is(response.getBirthDate().toString())))
        .andExpect(jsonPath("$.address", is(response.getAddress())))
        .andExpect(jsonPath("$.phoneNumber", is(response.getPhoneNumber())))
        .andExpect(jsonPath("$.createdAt", instantComparesEqualTo(response.getCreatedAt())))
        .andExpect(
            jsonPath("$.lastModifiedAt", instantComparesEqualTo(response.getLastModifiedAt())))
        .andExpect(jsonPath("$._links.self.href", is("/users/1")))
        .andExpect(jsonPath("$._links.update-all-fields.href", is("/users/1")))
        .andExpect(jsonPath("$._links.update-some-fields.href", is("/users/1")))
        .andExpect(jsonPath("$._links.delete.href", is("/users/1")));
  }

  @Test
  void shouldReturnErrorResponseWhenGetUserByIdAndNoSuchUserByIdException() throws Exception {
    // given
    when(userService.getById(1L)).thenThrow(new NoSuchUserByIdException(1L));
    // when
    mockMvc.perform(get("/users/1").contentType("application/json"))
        .andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.title", is("No such user")))
        .andExpect(jsonPath("$.detail", is("User with id 1 not found")))
        .andExpect(jsonPath("$.instance", is("/users/1")));
  }

  @Test
  void shouldReturnUserResponseWhenUpdateSomeUserFields() throws Exception {
    // given
    var timeOfCreationAndModification = Instant.now();
    var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890", timeOfCreationAndModification,
        timeOfCreationAndModification);
    response.add(Link.of("/users/1", "self"));
    response.add(Link.of("/users/1", "update-all-fields"));
    response.add(Link.of("/users/1", "update-some-fields"));
    response.add(Link.of("/users/1", "delete"));

    var request = new UserPartialUpdateRequest(null, null, null, null, "New address", null);

    when(userService.update(1L, request)).thenReturn(response);

    mockMvc.perform(patch("/users/1").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
        .andExpect(jsonPath("$.email", is(response.getEmail())))
        .andExpect(jsonPath("$.firstName", is(response.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(response.getLastName())))
        .andExpect(jsonPath("$.birthDate", is(response.getBirthDate().toString())))
        .andExpect(jsonPath("$.address", is(response.getAddress())))
        .andExpect(jsonPath("$.phoneNumber", is(response.getPhoneNumber())))
        .andExpect(jsonPath("$.createdAt", instantComparesEqualTo(response.getCreatedAt())))
        .andExpect(
            jsonPath("$.lastModifiedAt", instantComparesEqualTo(response.getLastModifiedAt())))
        .andExpect(jsonPath("$._links.self.href", is("/users/1")))
        .andExpect(jsonPath("$._links.update-all-fields.href", is("/users/1")))
        .andExpect(jsonPath("$._links.update-some-fields.href", is("/users/1")))
        .andExpect(jsonPath("$._links.delete.href", is("/users/1")));
  }

  @Test
  void shouldReturnErrorResponseWhenUpdateSomeUserFieldsAndNoSuchUserByIdException()
      throws Exception {
    // given
    var request = new UserPartialUpdateRequest(null, null, null, null, "New address", null);

    when(userService.update(1L, request)).thenThrow(new NoSuchUserByIdException(1L));
    // when
    mockMvc.perform(patch("/users/1").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404))).andExpect(jsonPath("$.title", is("No such user")))
        .andExpect(jsonPath("$.detail", is("User with id 1 not found")))
        .andExpect(jsonPath("$.instance", is("/users/1")));
  }

  @Test
  void shouldReturnUserResponseWhenUpdateAllUserFields() throws Exception {
    // given
    var timeOfCreationAndModification = Instant.now();
    var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890", timeOfCreationAndModification,
        timeOfCreationAndModification);
    response.add(Link.of("/users/1", "self"));
    response.add(Link.of("/users/1", "update-all-fields"));
    response.add(Link.of("/users/1", "update-some-fields"));
    response.add(Link.of("/users/1", "delete"));

    var request = new UserUpdateRequest("newemail@gmail.com", "New John", "New Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890");

    when(userService.update(1L, request)).thenReturn(response);

    mockMvc.perform(put("/users/1").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
        .andExpect(jsonPath("$.email", is(response.getEmail())))
        .andExpect(jsonPath("$.firstName", is(response.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(response.getLastName())))
        .andExpect(jsonPath("$.birthDate", is(response.getBirthDate().toString())))
        .andExpect(jsonPath("$.address", is(response.getAddress())))
        .andExpect(jsonPath("$.phoneNumber", is(response.getPhoneNumber())))
        .andExpect(jsonPath("$.createdAt", instantComparesEqualTo(response.getCreatedAt())))
        .andExpect(
            jsonPath("$.lastModifiedAt", instantComparesEqualTo(response.getLastModifiedAt())))
        .andExpect(jsonPath("$._links.self.href", is("/users/1")))
        .andExpect(jsonPath("$._links.update-all-fields.href", is("/users/1")))
        .andExpect(jsonPath("$._links.update-some-fields.href", is("/users/1")))
        .andExpect(jsonPath("$._links.delete.href", is("/users/1")));
  }

  @Test
  void shouldReturnErrorResponseWhenUpdateAllUserFieldsAndNoSuchUserByIdException()
      throws Exception {
    // given
    var request = new UserUpdateRequest("newemail@gmail.com", "New John", "New Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890");

    when(userService.update(1L, request)).thenThrow(new NoSuchUserByIdException(1L));
    // when
    mockMvc.perform(put("/users/1").contentType("application/json")
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404))).andExpect(jsonPath("$.title", is("No such user")))
        .andExpect(jsonPath("$.detail", is("User with id 1 not found")))
        .andExpect(jsonPath("$.instance", is("/users/1")));
  }

  @Test
  void shouldReturnNoContentWhenDeleteUser() throws Exception {
    mockMvc.perform(delete("/users/1").contentType("application/json"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundWhenDeleteNonExistingUser() throws Exception {
    doThrow(new NoSuchUserByIdException(1L)).when(userService).delete(1L);

    mockMvc.perform(delete("/users/1").contentType("application/json"))
        .andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.title", is("No such user")))
        .andExpect(jsonPath("$.detail", is("User with id 1 not found")))
        .andExpect(jsonPath("$.instance", is("/users/1")));
  }

  @Test
  void shouldReturnListOfUsersByBirthDateRange() throws Exception {
    // given
    var timeOfCreationAndModification = Instant.now();
    var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
        LocalDate.of(2004, 5, 20), "New address", "+1234567890", timeOfCreationAndModification,
        timeOfCreationAndModification);
    response.add(Link.of("/users/1", "self"));
    response.add(Link.of("/users/1", "update-all-fields"));
    response.add(Link.of("/users/1", "update-some-fields"));
    response.add(Link.of("/users/1", "delete"));
    var range = new RangeLocalDateCriteria(LocalDate.of(2003, 5, 20), LocalDate.of(2005, 5, 20));
    var page = new PageImpl<>(List.of(response));
    var pagedModel = PagedModel.of(page.getContent(),
        new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(),
            page.getTotalPages()));

    when(userService.getAll(eq(range), any())).thenReturn(pagedModel);

    mockMvc.perform(get("/users").contentType("application/json").param("from", "2003-05-20")
            .param("to", "2005-05-20")).andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.users[0].id", is(response.getId()), Long.class))
        .andExpect(jsonPath("$._embedded.users[0].email", is(response.getEmail())))
        .andExpect(jsonPath("$._embedded.users[0].firstName", is(response.getFirstName())))
        .andExpect(jsonPath("$._embedded.users[0].lastName", is(response.getLastName()))).andExpect(
            jsonPath("$._embedded.users[0].birthDate", is(response.getBirthDate().toString())))
        .andExpect(jsonPath("$._embedded.users[0].address", is(response.getAddress())))
        .andExpect(jsonPath("$._embedded.users[0].phoneNumber", is(response.getPhoneNumber())))
        .andExpect(jsonPath("$._embedded.users[0].createdAt",
            instantComparesEqualTo(response.getCreatedAt()))).andExpect(
            jsonPath("$._embedded.users[0].lastModifiedAt",
                instantComparesEqualTo(response.getLastModifiedAt())))
        .andExpect(jsonPath("$._embedded.users[0]._links.self.href", is("/users/1")));
  }

  @Test
  void shouldReturnErrorResponseWhenGetUsersByBirthDateRangeAndInvalidRange() throws Exception {

    mockMvc.perform(get("/users").contentType("application/json").param("from", "2005-05-20")
            .param("to", "2003-05-20")).andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.title", is("Bad Request")))
        .andExpect(jsonPath("$.instance", is("/users")))
        .andExpect(jsonPath("$.detail", containsString("Validation failed")))
        .andExpect(jsonPath("$.errors[0].object", is("rangeLocalDateCriteria")))
        .andExpect(
            jsonPath("$.errors[0].message", not(blankOrNullString())));
  }
}
