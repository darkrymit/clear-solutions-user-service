package com.clear.solutions.validation.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clear.solutions.validation.constraints.FirstName;
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
class LastNameValidationTest {

  @Autowired
  private Validator validator;


  @ParameterizedTest
  @ValueSource(strings = {"a", "Alice", "Robert Jr.", "Mark O'neil", "Thomas Müler", "ßáçøñ",
      "فلسطين", "Олег", "Hubert Wolfeschlegelsteinhausenbergerdorff"})
  void shouldBeValidWhenValidName(String name) {
    // given
    DataWithLastName data = new DataWithLastName(name);

    // when
    Set<ConstraintViolation<DataWithLastName>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeValidWhenNameIsNull() {
    // given
    DataWithLastName data = new DataWithLastName(null);

    // when
    Set<ConstraintViolation<DataWithLastName>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }


  @ParameterizedTest
  @ValueSource(strings = {"Alice123", "Alice@", "Alice@123", "Alice 123", "Alice 123@"})
  void shouldBeInvalidWhenNameInvalid(String name) {
    // given
    DataWithLastName data = new DataWithLastName(name);

    // when
    Set<ConstraintViolation<DataWithLastName>> violations = validator.validate(data);

    // then
    assertEquals(1, violations.size());
  }

  @Getter
  @RequiredArgsConstructor
  private static class DataWithLastName {

    @FirstName
    private final String firstName;

  }

}