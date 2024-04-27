package com.clear.solutions.validation.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clear.solutions.validation.constraints.Age;
import com.clear.solutions.validation.testspecific.ValidationUnitTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@ValidationUnitTest
@TestPropertySource(properties = {"application.constraints.user.age.min=18"})
class AgeValidationTest {

  @Autowired
  private Validator validator;


  @Test
  void shouldBeValidWhenAgeIsGreaterThanMinAge() {
    // given
    DataWithAge data = new DataWithAge(LocalDate.now().minusYears(20));

    // when
    Set<ConstraintViolation<DataWithAge>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeInvalidWhenAgeIsLessThanMinAge() {
    // given
    DataWithAge data = new DataWithAge(LocalDate.now().minusYears(10));

    // when
    Set<ConstraintViolation<DataWithAge>> violations = validator.validate(data);

    // then
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream()
        .anyMatch(violation -> violation.getMessage().contains("age greater than")));
  }

  @Test
  void shouldBeValidWhenAgeIsNull() {
    // given
    DataWithAge data = new DataWithAge(null);

    // when
    Set<ConstraintViolation<DataWithAge>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeValidWhenAgeIsEqualToMinAge() {
    // given
    DataWithAge data = new DataWithAge(LocalDate.now().minusYears(18));

    // when
    Set<ConstraintViolation<DataWithAge>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }


  @Getter
  @RequiredArgsConstructor
  private static class DataWithAge {

    @Age
    private final LocalDate age;

  }

}