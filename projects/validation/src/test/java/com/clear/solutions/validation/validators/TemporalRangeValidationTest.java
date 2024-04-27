package com.clear.solutions.validation.validators;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clear.solutions.validation.constraints.LocalDateRangeProvider;
import com.clear.solutions.validation.constraints.TemporalRange;
import com.clear.solutions.validation.testspecific.ValidationUnitTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ValidationUnitTest
class TemporalRangeValidationTest {

  @Autowired
  private Validator validator;


  @Test
  void shouldBeValidWhenSameDateAndAllowSameIsTrue() {
    // given
    DataWithLocalDateRange data = new DataWithLocalDateRange(LocalDate.now(), LocalDate.now());

    // when
    Set<ConstraintViolation<DataWithLocalDateRange>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeInvalidWhenSameDateAndAllowSameIsFalse() {
    // given
    DataWithDifferentLocalDateRange data = new DataWithDifferentLocalDateRange(LocalDate.now(),
        LocalDate.now());

    // when
    Set<ConstraintViolation<DataWithDifferentLocalDateRange>> violations = validator.validate(data);

    // then
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream()
        .anyMatch(violation -> violation.getMessage().contains("start date less than")));
  }

  @Test
  void shouldBeValidWhenFromDateIsBeforeToDate() {
    // given
    DataWithLocalDateRange data = new DataWithLocalDateRange(LocalDate.now(),
        LocalDate.now().plusDays(1));

    // when
    Set<ConstraintViolation<DataWithLocalDateRange>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeInvalidWhenFromDateIsAfterToDate() {
    // given
    DataWithLocalDateRange data = new DataWithLocalDateRange(LocalDate.now().plusDays(1),
        LocalDate.now());

    // when
    Set<ConstraintViolation<DataWithLocalDateRange>> violations = validator.validate(data);

    // then
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream()
        .anyMatch(violation -> violation.getMessage().contains("start date less or equal")));
  }

  @Test
  void shouldBeValidWhenFromDateIsNull() {
    // given
    DataWithLocalDateRange data = new DataWithLocalDateRange(null, LocalDate.now());

    // when
    Set<ConstraintViolation<DataWithLocalDateRange>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeValidWhenToDateIsNull() {
    // given
    DataWithLocalDateRange data = new DataWithLocalDateRange(LocalDate.now(), null);

    // when
    Set<ConstraintViolation<DataWithLocalDateRange>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldBeValidWhenBothDatesAreNull() {
    // given
    DataWithLocalDateRange data = new DataWithLocalDateRange(null, null);

    // when
    Set<ConstraintViolation<DataWithLocalDateRange>> violations = validator.validate(data);

    // then
    assertTrue(violations.isEmpty());
  }

  @Getter
  @RequiredArgsConstructor
  @TemporalRange
  private static class DataWithLocalDateRange implements LocalDateRangeProvider {

    private final LocalDate fromDate;

    private final LocalDate toDate;

    @Nullable
    @Override
    public LocalDate getFromTemporal() {
      return fromDate;
    }

    @Nullable
    @Override
    public LocalDate getToTemporal() {
      return toDate;
    }
  }

  @Getter
  @RequiredArgsConstructor
  @TemporalRange(allowSame = false)
  private static class DataWithDifferentLocalDateRange implements LocalDateRangeProvider {

    private final LocalDate fromDate;

    private final LocalDate toDate;

    @Nullable
    @Override
    public LocalDate getFromTemporal() {
      return fromDate;
    }

    @Nullable
    @Override
    public LocalDate getToTemporal() {
      return toDate;
    }
  }

}