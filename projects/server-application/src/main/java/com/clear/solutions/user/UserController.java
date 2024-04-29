package com.clear.solutions.user;


import com.clear.solutions.payload.range.RangeLocalDateCriteria;
import com.clear.solutions.payload.user.UserCreateRequest;
import com.clear.solutions.payload.user.UserPartialUpdateRequest;
import com.clear.solutions.payload.user.UserResponse;
import com.clear.solutions.payload.user.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = {"application/hal+json", "application/json",
    "application/problem+json"})
@Validated
public class UserController {

  private final UserService userService;

  @PostMapping(consumes = "application/json")
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
    var response = userService.create(request);
    return ResponseEntity.created(response.getRequiredLink("self").toUri()).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getById(id));
  }

  @PatchMapping(path = "/{id}", consumes = "application/json")
  public ResponseEntity<UserResponse> update(@PathVariable Long id,
      @Valid @RequestBody UserPartialUpdateRequest request) {
    return ResponseEntity.ok(userService.update(id, request));
  }

  @PutMapping(path = "/{id}", consumes = "application/json")
  public ResponseEntity<UserResponse> update(@PathVariable Long id,
      @Valid @RequestBody UserUpdateRequest request) {
    return ResponseEntity.ok(userService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<PagedModel<UserResponse>> getAll(@Valid RangeLocalDateCriteria range,
      @Valid Pageable pageable) {
    return ResponseEntity.ok(userService.getAll(range, pageable));
  }
}
