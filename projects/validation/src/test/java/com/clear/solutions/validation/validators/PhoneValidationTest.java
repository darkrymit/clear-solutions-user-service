package com.clear.solutions.validation.validators;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clear.solutions.validation.constraints.Phone;
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
class PhoneValidationTest {

  @Autowired
  private Validator validator;


  @ParameterizedTest
  @ValueSource(strings = {"+14155552671", "+380688622739", "+1447851987654"})
  void shouldBeValidWhenPhoneIsValid(String phone) {
    // given
    DataWithPhone data = new DataWithPhone(phone);

    // when
    Set<ConstraintViolation<DataWithPhone>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }


  @Test
  void shouldBeValidWhenPhoneIsNull() {
    // given
    DataWithPhone data = new DataWithPhone(null);

    // when
    Set<ConstraintViolation<DataWithPhone>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeInvalidWhenPhoneStartsWithZeroAfterPlus() {
    // given
    DataWithPhone data = new DataWithPhone("+014155552671");

    // when
    Set<ConstraintViolation<DataWithPhone>> violations = validator.validate(data);

    // then
    assertFalse(violations.isEmpty());
  }

  @Getter
  @RequiredArgsConstructor
  private static class DataWithPhone {

    @Phone
    private final String phone;

  }

}