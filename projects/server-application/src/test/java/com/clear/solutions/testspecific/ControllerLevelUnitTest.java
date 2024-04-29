package com.clear.solutions.testspecific;


import com.clear.solutions.config.HateoasConfiguration;
import com.clear.solutions.shared.GeneralControllerExceptionHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(properties = "spring.mvc.problem-details.enabled=true")
@Import({GeneralControllerExceptionHandler.class, HateoasConfiguration.class})
@ImportAutoConfiguration
public @interface ControllerLevelUnitTest {

  @AliasFor(attribute = "controllers", annotation = WebMvcTest.class) Class<?>[] controllers() default {};
}
