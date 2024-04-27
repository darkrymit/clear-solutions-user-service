package com.clear.solutions.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
// The phone number in the E.164 format
@Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
public @interface Phone {

  String message() default "{com.clear.solutions.validation.constraints.Phone.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
