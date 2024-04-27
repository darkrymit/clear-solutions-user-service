package com.clear.solutions.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.validator.constraints.Length;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Length(min = 1, max = 50)
// https://a-tokyo.medium.com/first-and-last-name-validation-for-forms-and-databases-d3edf29ad29d
@Pattern(regexp = "^([a-zA-Z\\xC0-\uFFFF]+([ \\-']?[a-zA-Z\\xC0-\uFFFF]+)*([.])?){1,2}$")
public @interface FirstName {

  String message() default "{com.clear.solutions.validation.constraints.FirstName.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
