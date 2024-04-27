package com.clear.solutions.validation.validators;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clear.solutions.validation.constraints.Email;
import com.clear.solutions.validation.testspecific.ValidationUnitTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ValidationUnitTest
class EmailValidationTest {

  @Autowired
  private Validator validator;


  @ParameterizedTest
  @ValueSource(strings = {"example+test@gmail.com", "example@yahoo.com",
      "example.test@hotmail.com"})
  void shouldBeValidWhenEmailIsValid(String email) {
    // given
    DataWithEmail data = new DataWithEmail(email);

    // when
    Set<ConstraintViolation<DataWithEmail>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeValidWhenEmailIsNull() {
    // given
    DataWithEmail data = new DataWithEmail(null);

    // when
    Set<ConstraintViolation<DataWithEmail>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeInvalidWhenEmailIsNotValid() {
    // given
    DataWithEmail data = new DataWithEmail("invalidEmail");

    // when
    Set<ConstraintViolation<DataWithEmail>> violations = validator.validate(data);

    // then
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream()
        .anyMatch(violation -> violation.getMessage().contains("must be a valid email address")));
  }

  @Getter
  @RequiredArgsConstructor
  private static class DataWithEmail {

    @Email
    private final String email;

  }

}