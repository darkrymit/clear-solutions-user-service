# Design decisions for the project

## 1. Project structure

The project is structured in a way that the code is separated into different modules. Each module is
responsible for a specific part of the project. This makes the code more organized and easier to
maintain.

## 2. Build systems

The project uses Gradle as the build system. Gradle is a powerful build tool that allows for easy
configuration and customization of the build process.

A Gradle wrapper is included in the project so that developers do not need to install Gradle on
their machines. The Gradle wrapper will automatically download and install the correct version of
Gradle when the project is built.

Kotlin DSL is used for the Gradle build scripts. Kotlin DSL provides a more concise and type-safe
way to define build scripts compared to the traditional Groovy DSL.

Also, the project uses included builds to define the dependencies between the different modules.
This allows for better separation of concerns and makes it easier to manage the dependencies between
the modules. Also, it allows for better reuse of the build scripts across different projects.

## 3. Validation

The project uses the Hibernate Validator library for input validation. Hibernate Validator is a
popular validation library that provides a rich set of validation constraints and is easy to use.

Also, Hibernate Validator is the reference implementation of the Bean Validation API. This means
that
it is well-maintained and up to date with the latest features of the Bean Validation API.

Also, custom validation annotations are used to validate the input data. This makes the code more
readable and maintainable by encapsulating the validation logic in a separate module that can be
reused across the project.

## 4. Error handling

The project uses the Spring Boot implementation of the Problem Details for HTTP APIs specification
also known as RFC 7807. This specification defines a way to carry machine-readable details of errors
in an HTTP response to avoid the need to invent new error response formats for HTTP APIs.
More details can be found [here](https://tools.ietf.org/html/rfc7807).

Also, the project uses the `@ControllerAdvice` annotation to define global exception handlers. This
allows using custom exception handlers for different types of exceptions and to centralize the
error handling logic in a single place and provide a consistent error response format across the
API.

## 5. Testing

The project uses JUnit 5 for unit testing and Testcontainers for integration testing. JUnit 5 is the
latest version of the popular JUnit testing framework and provides many new features and
improvements over JUnit 4.

Testcontainers is a Java library that provides lightweight, throwaway instances of common databases,
Selenium web browsers, or anything else that can run in a Docker container. It is a great tool for
integration testing as it allows for easy setup and teardown of test environments.

## 6. CI

The project uses GitHub Actions for continuous integration. GitHub Actions is a powerful CI/CD tool
that is tightly integrated with GitHub and allows for easy setup of CI/CD pipelines.

Also, to simplify test results reporting, the project uses dorny/test-reporter GitHub Action. This
is a GitHub Action that parses test results and provides a well-formatted summary of the test
results that can be easily viewed in the GitHub UI.


