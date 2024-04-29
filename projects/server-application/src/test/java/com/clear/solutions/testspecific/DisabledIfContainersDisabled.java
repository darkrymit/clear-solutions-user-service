package com.clear.solutions.testspecific;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.test.context.junit.jupiter.DisabledIf;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DisabledIf(expression = "#{T(java.lang.Boolean).valueOf(systemProperties['application.test.containers.disabled'])}")
public @interface DisabledIfContainersDisabled {

}
