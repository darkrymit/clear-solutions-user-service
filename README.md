# Cleat Solutions test assignment [![CI](https://github.com/darkrymit/clear-solutions-user-service/actions/workflows/main.yml/badge.svg?branch=main)](https://github.com/darkrymit/clear-solutions-user-service/actions/workflows/main.yml)

This repository contains the implementation of the test assignment for the Java practical test
assignment from Clear Solutions. The task is to implement a RESTful API based on a web Spring Boot.

For more details, please refer to the [task description](task.md).

For more details on design decisions, please refer to the [design decisions](design-decisions.md).

## Table of Contents

- [Getting Started](#getting-started)
    - [Requirements](#requirements)
    - [How to run](#how-to-run)
- [Running the tests](#running-the-tests)
    - [Unit and Integration tests](#unit-and-integration-tests)
- [Endpoints](#endpoints)
- [Built With](#built-with)

## Getting Started

### Requirements

- Java 17 or higher (mandatory) (JDK
  preferred) [Install](https://www.oracle.com/java/technologies/downloads/#java17)
- Docker engine (mandatory) (Needed for running the database
  container for test and development) [Install](https://docs.docker.com/get-docker/)

### How to run

The 1st and 2nd steps are optional and can be skipped if you have already cloned the repository.
The steps after the 2nd step are mandatory.

Steps:

1. Clone the repository
    ```shell
    git clone https://github.com/darkrymit/clear-solutions-user-service.git clear-solutions-user-service
   ```
2. Change directory to the project root
    ```shell
    cd clear-solutions-user-service
    ```
3. Build the project using Gradle wrapper
    ```shell
    ./gradlew build
    ```
   Note: Requires Docker engine to be running on your machine to use database container for testing.

   If you don't have Docker installed, you can skip the tests that require the database by running
   the following command:
    ```shell
    ./gradlew build -Dapplication.test.containers.disabled=true
    ```
4. Run the project

   #### Using bootRun (Runs the project using java installed on your machine)
    ```shell
    ./gradlew bootRun
    ```
   Note: Requires PostgreSQL database to be running on your machine.
   Configure the database connection by setting the following environment variables:
    - SPRING_DATASOURCE_URL (default: jdbc:postgresql://localhost:5432/user-service)
    - SPRING_DATASOURCE_USERNAME (default: postgres)
    - SPRING_DATASOURCE_PASSWORD (default: postgres)

   #### Using Docker (Runs the project using the Docker image built by the Gradle task)

   Build the Docker image
    ```shell
    ./gradlew bootBuildImage
    ```
   Run the Docker Compose file to start the project and required services
    ```shell
    docker-compose -f projects/server-application/src/docker/app.yml up 
    ```
   Note: Requires Docker engine to be running on your machine.
5. Check the application status by visiting the following URL in your browser:

   #### Using curl:
    ```shell
    curl http://localhost:8080/management/health/readiness
    ```
   #### Using browser:

   [http://localhost:8080/management/health/readiness](http://localhost:8080/management/health/readiness)

   You should see a JSON response with the status of the application.

## Running the tests

### Unit and Integration tests

When you build the project, the unit and integration tests are run automatically.

You can also run
the tests manually
using the following command:

```shell
./gradlew test
```

Note: Requires Docker engine to be running on your machine to use database container for testing.

If you want to run the tests without the database container, you can run the following command:

```shell
./gradlew test -Dapplication.test.containers.disabled=true
```

Note: The tests that require the database will be skipped.

### Mock data

The project uses Flyway to manage the database schema and migrations.
The project contains a script that inserts mock data into the database based on activated profiles.

The script is located
at [projects/server-application/src/main/resources/db/mock-data/afterMigrate.sql](projects/server-application/src/main/resources/db/mock-data/afterMigrate.sql).

The Data contains more than 10 users with cyrillic, latin in the name and surname, and different
email addresses.

## Endpoints

Brief description of the implemented endpoints:

#### Create User

* **HTTP Method:** POST
* **Endpoint:** /api/users
* **Consumes:** JSON (application/json)
* **Produces:** JSON (application/json)
* **Request Body:** A JSON object representing user details (UserCreateRequest)
* **Response:** Returns a (UserResponse) representing the created user.
* **HTTP Status:** 201 Created
* **Example Request Body:**
  ```json
  {
    "email": "Araceli_Bauch82@yahoo.com",
    "firstName": "Krystina",
    "lastName": "Roob",
    "birthDate": "2002-02-02",
    "address": "94315 Chyna Square",
    "phoneNumber": "+380688633228"
  }
  ```
* **Example Response Body:**
  ```json
  {
    "id": 13,
    "email": "Araceli_Bauch82@yahoo.com",
    "firstName": "Krystina",
    "lastName": "Roob",
    "birthDate": "2002-02-02",
    "address": "94315 Chyna Square",
    "phoneNumber": "+380688633228",
    "createdAt": "2024-04-29T12:19:19.694517081Z",
    "lastModifiedAt": "2024-04-29T12:19:19.694517081Z",
    "_links": {
        "self": {
            "href": "/users/13"
        },
        "update": {
            "href": "/users/13"
        },
        "update-partial": {
            "href": "/users/13"
        },
        "delete": {
            "href": "/users/13"
        }
    }
  }
  ```

#### Get User by Id

* **HTTP Method:** GET
* **Endpoint:** /api/users/{userId}
* **Consumes:** JSON (application/json)
* **Produces:** JSON (application/json)
* **Path Variable:** userId - The ID of the user to get.
* **Response:** Returns a (UserResponse) representing the user.
* **HTTP Status:** 200 OK
* **Example Response Body:**
   ```json
   {
     "id": 2,
     "email": "john.doe@example.com",
     "firstName": "John",
     "lastName": "Doe",
     "birthDate": "1980-01-01",
     "address": "123 Main St",
     "phoneNumber": "+1234567890",
     "createdAt": "2024-03-25T22:22:09.266615Z",
     "lastModifiedAt": "2024-03-25T22:28:19.266615Z",
     "_links": {
         "self": {
             "href": "/users/2"
         },
         "update": {
             "href": "/users/2"
         },
         "update-partial": {
             "href": "/users/2"
         },
         "delete": {
             "href": "/users/2"
         }
     }
   }
   ```

#### Update Some User Fields

* **HTTP Method:** PATCH
* **Endpoint:** /api/users/{userId}
* **Consumes:** JSON (application/json)
* **Produces:** JSON (application/json)
* **Path Variable:** userId - The ID of the user to update.
* **Request Body:** A JSON object representing the fields to update (UserPartialUpdateRequest).
* **Response:** Returns a (UserResponse) representing the updated user.
* **HTTP Status:** 200 OK
* **Example Request Body:**
  ```json
  {
    "firstName":"Jack"
  }
  ```
* **Example Response Body:**
  ```json
  {
    "id": 2,
    "email": "john.doe@example.com",
    "firstName": "Jack",
    "lastName": "Doe",
    "birthDate": "1980-01-01",
    "address": "123 Main St",
    "phoneNumber": "+1234567890",
    "createdAt": "2024-03-25T22:22:09.266615Z",
    "lastModifiedAt": "2024-04-29T12:26:16.735067679Z",
    "_links": {
        "self": {
            "href": "/users/2"
        },
        "update": {
            "href": "/users/2"
        },
        "update-partial": {
            "href": "/users/2"
        },
        "delete": {
            "href": "/users/2"
        }
    }
  }
  ```

#### Update All User Fields

* **HTTP Method:** PUT
* **Endpoint:** /api/users/{userId}
* **Consumes:** JSON (application/json)
* **Produces:** JSON (application/json)
* **Path Variable:** userId - The ID of the user to update.
* **Request Body:** A JSON object representing all user fields to update (UserUpdateRequest).
* **Response:** Returns a (UserResponse) representing the updated user.
* **HTTP Status:** 200 OK
* **Example Request Body:**
  ```json
  {
    "email": "Reva77@yahoo.com",
    "firstName": "Filomena",
    "lastName": "Goodwin",
    "birthDate": "2002-02-02",
    "address": "0970 Vada Estate",
    "phoneNumber": "+380688633258"
  }
  ```
* **Example Response Body:**
  ```json
  {
    "id": 2,
    "email": "Reva77@yahoo.com",
    "firstName": "Filomena",
    "lastName": "Goodwin",
    "birthDate": "2002-02-02",
    "address": "0970 Vada Estate",
    "phoneNumber": "+380688633258",
    "createdAt": "2024-03-25T22:22:09.266615Z",
    "lastModifiedAt": "2024-04-29T12:28:25.347403143Z",
    "_links": {
        "self": {
            "href": "/users/2"
        },
        "update": {
            "href": "/users/2"
        },
        "update-partial": {
            "href": "/users/2"
        },
        "delete": {
            "href": "/users/2"
        }
    }
  }
  ```

#### Delete User

* **HTTP Method:** DELETE
* **Endpoint:** /api/users/{userId}
* **Path Variable:** userId - The ID of the user to delete.
* **Response:** Deletes the user with the specified ID.
* **HTTP Status:** 204 No Content

#### Search for Users by Birth Date Range

* **HTTP Method:** GET
* **Endpoint:** /api/users
* **Request Parameters:**
* from (Required): The start date of the birth date range in ISO date format (yyyy-MM-dd).
* to (Required): The end date of the birth date range in ISO date format (yyyy-MM-dd).
* **Response:** Returns a List of UserResponses within the specified birth date range.
* **HTTP Status:** 200 OK
* **Example Request:**
  ```shell
  curl --location 'localhost:8080/users?from=2000-01-01&to=2002-01-01' \
  --header 'Content-Type: application/json'
  ```
* **Example Response:**
  ```json
  {
    "_embedded": {
        "users": [
            {
                "id": 6,
                "email": "null.user@example.com",
                "firstName": "Null",
                "lastName": "Nullable",
                "birthDate": "2000-01-01",
                "address": null,
                "phoneNumber": null,
                "createdAt": "2024-03-29T22:22:09.266615Z",
                "lastModifiedAt": "2024-03-29T22:28:19.266615Z",
                "_links": {
                    "self": {
                        "href": "/users/6"
                    },
                    "update": {
                        "href": "/users/6"
                    },
                    "update-partial": {
                        "href": "/users/6"
                    },
                    "delete": {
                        "href": "/users/6"
                    }
                }
            },
            {
                "id": 11,
                "email": "john.doe.jr@example.com",
                "firstName": "John.Doe",
                "lastName": "Jr",
                "birthDate": "2000-01-01",
                "address": "123 Main St",
                "phoneNumber": "+1234567890",
                "createdAt": "2024-04-03T22:22:09.266615Z",
                "lastModifiedAt": "2024-04-03T22:28:19.266615Z",
                "_links": {
                    "self": {
                        "href": "/users/11"
                    },
                    "update": {
                        "href": "/users/11"
                    },
                    "update-partial": {
                        "href": "/users/11"
                    },
                    "delete": {
                        "href": "/users/11"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "/users?page=0&size=20"
        }
    },
    "page": {
        "size": 20,
        "totalElements": 2,
        "totalPages": 1,
        "number": 0
    }
  }
  ```

## Built With

The project is built using the following technologies:

- [Spring Boot](https://spring.io/projects/spring-boot) - The base framework
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Data access framework
- [Spring Actuator](https://spring.io/guides/gs/actuator-service/) - Monitoring and management
  framework
- [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-devtools) -
  Development tools
- [Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing) -
  Testing framework
- [Spring Boot Validation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-validation) -
  Validation framework
- [Spring Boot Web](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-developing-web-applications) -
  Web framework
- [Spring Boot HATEOAS](https://docs.spring.io/spring-hateoas/docs/current/reference/html/) -
  HATEOAS support
- [Gradle](https://gradle.org/) - Dependency Management
- [PostgreSQL](https://www.postgresql.org/) - Database
- [Flyway](https://flywaydb.org/) - Database schema and migration management
- [Docker](https://www.docker.com/) - Containerization
- [JUnit 5](https://junit.org/junit5/) - Testing framework
- [Mockito](https://site.mockito.org/) - Mocking framework
- [Testcontainers](https://www.testcontainers.org/) - Integration testing framework
- [Lombok](https://projectlombok.org/) - Code generation library
- [MapStruct](https://mapstruct.org/) - Mapping library
