package com.clear.solutions.payload.user;

import com.clear.solutions.validation.constraints.Age;
import com.clear.solutions.validation.constraints.Email;
import com.clear.solutions.validation.constraints.FirstName;
import com.clear.solutions.validation.constraints.Id;
import com.clear.solutions.validation.constraints.LastName;
import com.clear.solutions.validation.constraints.Phone;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "users", itemRelation = "user")
public class UserResponse extends RepresentationModel<UserResponse> {

  @Id
  @NotNull
  private Long id;

  @Email
  @NotNull
  private String email;

  @FirstName
  @NotNull
  private String firstName;

  @LastName
  @NotNull
  private String lastName;

  @Age
  @NotNull
  private LocalDate birthDate;

  @Nullable
  private String address;

  @Phone
  @Nullable
  private String phoneNumber;

  @NotNull
  private Instant createdAt;

  @NotNull
  private Instant lastModifiedAt;
}
