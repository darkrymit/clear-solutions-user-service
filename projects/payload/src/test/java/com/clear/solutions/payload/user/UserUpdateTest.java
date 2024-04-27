package com.clear.solutions.payload.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clear.solutions.payload.testspecific.PayloadUnitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@PayloadUnitTest
class UserUpdateTest {

  @Autowired
  ObjectMapper objectMapper;

  @Test
  void shouldBeSerializableToJSON() throws JsonProcessingException {
    // given
    var request = new UserUpdateRequest("example@gmail.com", "firstName", "lastName",
        LocalDate.EPOCH, "Some address", "+123456789");
    // when
    String json = objectMapper.writeValueAsString(request);

    // then
    assertTrue(json.contains("example@gmail.com"));
    assertTrue(json.contains("firstName"));
    assertTrue(json.contains("lastName"));
    assertTrue(json.contains("birthDate"));
    assertTrue(json.contains("Some address"));
    assertTrue(json.contains("+123456789"));
  }


  @Test
  void shouldBeDeserializableFromJSON() throws JsonProcessingException {
    // given
    var request = new UserUpdateRequest("example@gmail.com", "firstName", "lastName",
        LocalDate.EPOCH, "Some address", "+123456789");
    String json = objectMapper.writeValueAsString(request);

    // when
    var deserializedRequest = objectMapper.readValue(json, UserUpdateRequest.class);

    // then
    assertEquals(request, deserializedRequest);
  }

  @Test
  void shouldImplementEqualsAndHashCodeCorrectly() {
    EqualsVerifier.simple().forClass(UserUpdateRequest.class).verify();
  }
}