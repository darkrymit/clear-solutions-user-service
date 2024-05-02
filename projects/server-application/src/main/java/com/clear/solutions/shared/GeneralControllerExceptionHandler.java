package com.clear.solutions.shared;

import com.clear.solutions.user.exception.NoSuchUserByIdException;
import com.clear.solutions.user.exception.UserAlreadyExistsByEmailException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.BindErrorUtils;

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

  @Override
  @Nullable
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    List<ObjectError> allObjectErrors = ex.getBindingResult().getAllErrors();

    var problemDetail = this.createValidationProblemDetail(ex, status, request, allObjectErrors,
        allObjectErrors, List.of());

    return this.handleExceptionInternal(ex, problemDetail, headers, status, request);
  }

  @Override
  @Nullable
  protected ResponseEntity<Object> handleHandlerMethodValidationException(
      HandlerMethodValidationException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    List<MessageSourceResolvable> messages = new ArrayList<>();
    List<ObjectError> allObjectErrors = new ArrayList<>();
    List<ParameterValidationResult> allParameterValidationResults = new ArrayList<>();

    ex.visitResults(new HandlerMethodVisitor(messages, allObjectErrors,
        allParameterValidationResults));

    var problemDetail = this.createValidationProblemDetail(ex, status, request, messages,
        allObjectErrors, allParameterValidationResults);

    return this.handleExceptionInternal(ex, problemDetail, headers, status, request);
  }

  @ExceptionHandler(value = {Exception.class})
  public ProblemDetail handleUnknownException(Exception e) {
    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
        String.format("Internal server error: %s", e.getMessage()));
    problemDetail.setTitle("Internal server error");
    return problemDetail;
  }

  private ProblemDetail createValidationProblemDetail(Exception ex,
      HttpStatusCode status, WebRequest request,
      List<? extends MessageSourceResolvable> messages,
      List<ObjectError> allObjectErrors,
      List<ParameterValidationResult> allParameterValidationResults) {
    Object[] args = new Object[]{BindErrorUtils.resolveAndJoin(messages)};
    String defaultDetail = String.format("Validation failed: %s", args[0]);
    var problemDetail = this.createProblemDetail(ex, status, defaultDetail, "validation.failed",
        args, request);

    Map<String, Object> properties = new HashMap<>();
    properties.put("errors", getErrors(allObjectErrors, allParameterValidationResults));
    problemDetail.setProperties(properties);
    return problemDetail;
  }

  private List<Object> getErrors(List<ObjectError> allObjectErrors,
      List<ParameterValidationResult> allParameterValidationResults) {
    List<Object> errors = new ArrayList<>();
    for (ParameterValidationResult result : allParameterValidationResults) {
      for (MessageSourceResolvable resolvable : result.getResolvableErrors()) {
        Map<String, Object> error = new HashMap<>();
        error.put("parameter", result.getMethodParameter().getParameterName());
        error.put("message", resolvable.getDefaultMessage());
        errors.add(error);
      }
    }
    for (ObjectError error : allObjectErrors) {
      Map<String, Object> errorMap = new HashMap<>();
      if (error instanceof FieldError fieldError) {
        errorMap.put("object", fieldError.getObjectName());
        errorMap.put("field", fieldError.getField());
      } else {
        errorMap.put("object", error.getObjectName());
      }
      errorMap.put("message", error.getDefaultMessage());
      errors.add(errorMap);
    }
    return errors;
  }

  public static class HandlerMethodVisitor implements HandlerMethodValidationException.Visitor {

    private final List<MessageSourceResolvable> messages;

    private final List<ObjectError> allObjectErrors;

    private final List<ParameterValidationResult> allParameterValidationResults;

    public HandlerMethodVisitor(List<MessageSourceResolvable> messages,
        List<ObjectError> allObjectErrors,
        List<ParameterValidationResult> allParameterValidationResults) {
      this.messages = messages;
      this.allObjectErrors = allObjectErrors;
      this.allParameterValidationResults = allParameterValidationResults;
    }

    private void add(ParameterValidationResult result) {
      messages.addAll(result.getResolvableErrors());
      allParameterValidationResults.add(result);
    }

    private void add(ParameterErrors errors) {
      messages.addAll(errors.getAllErrors());
      allObjectErrors.addAll(errors.getAllErrors());
    }

    @Override
    public void cookieValue(CookieValue cookieValue, ParameterValidationResult result) {
      add(result);
    }

    @Override
    public void matrixVariable(MatrixVariable matrixVariable, ParameterValidationResult result) {
      add(result);
    }

    @Override
    public void modelAttribute(@Nullable ModelAttribute modelAttribute, ParameterErrors errors) {
      add(errors);
    }

    @Override
    public void pathVariable(PathVariable pathVariable, ParameterValidationResult result) {
      add(result);
    }

    @Override
    public void requestBody(RequestBody requestBody, ParameterErrors errors) {
      add(errors);
    }

    @Override
    public void requestHeader(RequestHeader requestHeader, ParameterValidationResult result) {
      add(result);
    }

    @Override
    public void requestParam(@Nullable RequestParam requestParam,
        ParameterValidationResult result) {
      add(result);
    }

    @Override
    public void requestPart(RequestPart requestPart, ParameterErrors errors) {
      add(errors);
    }

    @Override
    public void other(ParameterValidationResult result) {
      add(result);
    }
  }
}
