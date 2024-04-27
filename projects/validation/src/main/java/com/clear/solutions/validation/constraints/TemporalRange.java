package com.clear.solutions.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.clear.solutions.validation.validators.LocalDateRangeProviderValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation defines a constraint for validating a date range. The date range is defined by a
 * from and to date. The type of the date range is defined by the generic parameter T. T must extend
 * Temporal and implement Comparable.
 *
 * <p>Since unfortunately, the validation API does not support generic types and to compare the
 * generic type T needed, currently the supported types are {@link java.time.LocalDate}.
 */
@Documented
@Constraint(validatedBy = {LocalDateRangeProviderValidator.class})
@Target({TYPE_USE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface TemporalRange {

  String message() default "{com.clear.solutions.validation.constraints.TemporalRange.message}";

  boolean allowSame() default true;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}