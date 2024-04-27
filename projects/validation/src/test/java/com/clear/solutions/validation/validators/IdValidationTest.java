package com.clear.solutions.validation.validators;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clear.solutions.validation.constraints.Id;
import com.clear.solutions.validation.testspecific.ValidationUnitTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ValidationUnitTest
class IdValidationTest {

  @Autowired
  private Validator validator;


  @Test
  void shouldBeValidWhenIdIsNotNull() {
    // given
    DataWithId data = new DataWithId(2L);

    // when
    Set<ConstraintViolation<DataWithId>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeValidWhenIdIsNull() {
    // given
    DataWithId data = new DataWithId(null);

    // when
    Set<ConstraintViolation<DataWithId>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Getter
  @RequiredArgsConstructor
  private static class DataWithId {

    @Id
    private final Long id;

  }

}