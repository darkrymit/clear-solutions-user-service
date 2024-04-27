package com.clear.solutions.validation.testspecific;

import java.util.Map;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

@Configuration
public class ValidatorTestConfig {

  @Bean
  public LocalValidatorFactoryBean validator(final AutowireCapableBeanFactory beanFactory) {
    LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
    validatorFactoryBean.setConstraintValidatorFactory(
        new SpringConstraintValidatorFactory(beanFactory));
    return validatorFactoryBean;
  }

  @Bean
  public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
      LocalValidatorFactoryBean validator) {
    return (Map<String, Object> hibernateProperties) -> hibernateProperties.put(
        "javax.persistence.validation.factory", validator);
  }

}