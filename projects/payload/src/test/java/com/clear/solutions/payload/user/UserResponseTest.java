package com.clear.solutions.payload.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clear.solutions.payload.testspecific.PayloadUnitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;

@PayloadUnitTest
class UserResponseTest {

  @Autowired
  ObjectMapper objectMapper;

  @Test
  void shouldBeSerializableToJSON() throws JsonProcessingException {
    // given
    var request = new UserResponse(1L, "email@gmail.com", "firstName", "lastName", LocalDate.EPOCH,
        "Some address", "+123456789", Instant.now(), Instant.now());

    // when
    var json = objectMapper.writeValueAsString(request);

    // then
    assertTrue(json.contains("email@gmail.com"));
    assertTrue(json.contains("firstName"));
    assertTrue(json.contains("lastName"));
    assertTrue(json.contains("birthDate"));
    assertTrue(json.contains("Some address"));
    assertTrue(json.contains("+123456789"));
  }


  @Test
  void shouldBeDeserializableFromJSON() throws JsonProcessingException {
    // given
    var request = new UserResponse(1L, "email@gmail.com", "firstName", "lastName", LocalDate.EPOCH,
        "Some address", "+123456789", Instant.now(), Instant.now());
    var json = objectMapper.writeValueAsString(request);

    // when
    var deserializedRequest = objectMapper.readValue(json, UserResponse.class);

    // then
    assertEquals(request, deserializedRequest);
  }

  @Test
  void shouldImplementEqualsAndHashCodeCorrectly() {
    EqualsVerifier.simple().forClass(UserResponse.class).withPrefabValues(Link.class,
            Link.of(":8080/api/v1/users/1", LinkRelation.of("self")),
            Link.of(":8080/api/v1/users/2", LinkRelation.of("self")))
        // There is a problem with HATEOAS RepresentationModel links field
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

}