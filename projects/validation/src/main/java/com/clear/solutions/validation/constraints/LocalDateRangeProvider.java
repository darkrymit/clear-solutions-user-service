package com.clear.solutions.validation.constraints;

import jakarta.annotation.Nullable;
import java.time.LocalDate;

/**
 * This interface extends the TemporalRangeProvider interface with LocalDate as the type parameter.
 * It provides a contract for providing a date range with LocalDate objects.
 * The date range is defined by a from and to date.
 * The from and to dates can be null, indicating an open-ended date range.
 */
public interface LocalDateRangeProvider extends TemporalRangeProvider<LocalDate> {

  /**
   * Returns the start date of the date range.
   *
   * <p> Can be null if the date range is open-ended. Then the validation of range disabled.
   *
   * @return the start date of the date range, or null if the date range is open-ended
   */
  @Nullable
  LocalDate getFromTemporal();

  /**
   * Returns the end date of the date range.
   *
   * <p> Can be null if the date range is open-ended. Then the validation of range disabled.
   *
   * @return the end date of the date range, or null if the date range is open-ended
   */
  @Nullable
  LocalDate getToTemporal();
}