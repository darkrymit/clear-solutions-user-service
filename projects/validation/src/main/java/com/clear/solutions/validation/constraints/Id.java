package com.clear.solutions.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Max(Integer.MAX_VALUE)
public @interface Id {

  String message() default "{com.clear.solutions.validation.constraints.Id.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
