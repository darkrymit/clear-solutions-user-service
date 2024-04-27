package com.clear.solutions.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.clear.solutions.validation.validators.AgeLocalDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {AgeLocalDateValidator.class})
@Target({TYPE_USE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface Age {

  String message() default "{com.clear.solutions.validation.constraints.Age.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
