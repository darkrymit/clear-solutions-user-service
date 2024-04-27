package com.clear.solutions.shared;

import com.clear.solutions.user.exception.NoSuchUserByIdException;
import com.clear.solutions.user.exception.UserAlreadyExistsByEmailException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GeneralControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {NoSuchUserByIdException.class})
  public ProblemDetail handleNoSuchUserByIdException(NoSuchUserByIdException e) {
    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        String.format("User with id %d not found", e.getId()));
    problemDetail.setTitle("No such user");
    problemDetail.setInstance(URI.create("/users/" + e.getId()));
    return problemDetail;
  }

  @ExceptionHandler(value = {UserAlreadyExistsByEmailException.class})
  public ProblemDetail handleUserAlreadyExistsByEmailException(
      UserAlreadyExistsByEmailException e) {
    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
        String.format("User with email %s already exists", e.getEmail()));
    problemDetail.setTitle("User already exists");
    return problemDetail;
  }

  @ExceptionHandler(value = {Exception.class})
  public ProblemDetail handleUnknownException(Exception e) {
    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
        String.format("Internal server error: %s", e.getMessage()));
    problemDetail.setTitle("Internal server error");
    return problemDetail;
  }
}
