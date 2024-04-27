package com.clear.solutions.payload.range;

import com.clear.solutions.validation.constraints.LocalDateRangeProvider;
import com.clear.solutions.validation.constraints.TemporalRange;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@TemporalRange(allowSame = false)
public class RangeLocalDateCriteria implements LocalDateRangeProvider {

  @Nullable
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate from;

  @Nullable
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate to;

  @Nullable
  @Override
  public LocalDate getFromTemporal() {
    return from;
  }

  @Nullable
  @Override
  public LocalDate getToTemporal() {
    return to;
  }
}
