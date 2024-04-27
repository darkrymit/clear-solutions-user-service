package com.clear.solutions.validation.validators;

import com.clear.solutions.validation.constraints.Age;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class AgeLocalDateValidator implements ConstraintValidator<Age, LocalDate> {

  private final int minAge;

  // Injecting the minAge value from the properties if it exists,
  // If it doesn't exist, the default value of 18 will be used
  public AgeLocalDateValidator(@Value("${application.constraints.user.age.min:18}") int minAge) {
    this.minAge = minAge;
  }

  @Override
  public void initialize(Age constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(@Nullable LocalDate value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    // Unwrapping the context to get the Hibernate specific context
    // Safe since Hibernate Validation is the only implementation of JPA Bean Validation
    HibernateConstraintValidatorContext hibernateContext = context.unwrap(
        HibernateConstraintValidatorContext.class);

    // Checking if the age is greater than or equal to the minAge
    // Not entirely accurate, but good enough for this example
    if (Period.between(value, LocalDate.now()).getYears() >= minAge) {
      return true;
    }

    // Adding the minAge variable to use in the error message
    // Needed to use since minAge is injected and not a constant
    hibernateContext.addMessageParameter("minAge", minAge);

    // Value is less than the minAge
    return false;
  }
}
