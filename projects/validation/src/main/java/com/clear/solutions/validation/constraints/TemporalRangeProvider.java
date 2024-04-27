package com.clear.solutions.validation.constraints;

import jakarta.annotation.Nullable;
import java.time.temporal.Temporal;

/**
 * This interface defines a contract for providing a date range. The date range is defined by a from
 * and to date. The type of the date range is defined by the generic parameter T. T must extend
 * Temporal and implement Comparable.
 *
 * <p>Since unfortunately, the validation API does not support generic types,
 * don't use this interface directly and prefer to use the specific date range provider aka
 * {@link LocalDateRangeProvider}.
 *
 * @param <T> the type of the date range
 */
public interface TemporalRangeProvider<T extends Temporal & Comparable<? super T>> {

  /**
   * Returns the start date of the date range.
   *
   * <p> Can be null if the date range is open-ended. Then the validation of range disabled.
   *
   * @return the start date of the date range, or null if the date range is open-ended
   */
  @Nullable
  T getFromTemporal();

  /**
   * Returns the end date of the date range.
   *
   * <p> Can be null if the date range is open-ended. Then the validation of range disabled.
   *
   * @return the end date of the date range, or null if the date range is open-ended
   */
  @Nullable
  T getToTemporal();
}