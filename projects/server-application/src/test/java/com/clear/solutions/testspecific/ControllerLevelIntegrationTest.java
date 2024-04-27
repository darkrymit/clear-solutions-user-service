package com.clear.solutions.testspecific;


import com.clear.solutions.UserServerApplication;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
    UserServerApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT, properties = "spring.flyway.locations=classpath:db/migration,classpath:db/test-data")
@ImportAutoConfiguration
@AutoConfigureTestDatabaseContainer
@AutoConfigureMockMvc
@ActiveProfiles("test")
public @interface ControllerLevelIntegrationTest {

}
