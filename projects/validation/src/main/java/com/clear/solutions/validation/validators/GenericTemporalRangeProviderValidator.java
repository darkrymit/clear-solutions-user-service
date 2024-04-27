package com.clear.solutions.validation.validators;

import com.clear.solutions.validation.constraints.TemporalRange;
import com.clear.solutions.validation.constraints.TemporalRangeProvider;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.temporal.Temporal;

/**
 * This validator validates a {@link TemporalRangeProvider} where the date range type provided by the
 * generic parameter T must extend Temporal and implement Comparable.
 */
public class GenericTemporalRangeProviderValidator<P extends TemporalRangeProvider<T>, T extends Temporal & Comparable<? super T>> implements
    ConstraintValidator<TemporalRange, P> {

  private boolean allowSame;

  @Override
  public void initialize(TemporalRange constraintAnnotation) {
    this.allowSame = constraintAnnotation.allowSame();
  }

  @Override
  public boolean isValid(@Nullable P dateRangeProvider,
      ConstraintValidatorContext constraintValidatorContext) {
    if (dateRangeProvider == null) {
      return true;
    }
    if (dateRangeProvider.getFromTemporal() == null || dateRangeProvider.getToTemporal() == null) {
      return true;
    }
    if (allowSame && isSame(dateRangeProvider.getFromTemporal(), dateRangeProvider.getToTemporal())) {
      return true;
    }
    return isBefore(dateRangeProvider.getFromTemporal(), dateRangeProvider.getToTemporal());
  }

  private boolean isSame(T from, T to) {
    return from.compareTo(to) == 0;
  }

  private boolean isBefore(T from, T to) {
    return from.compareTo(to) < 0;
  }
}
