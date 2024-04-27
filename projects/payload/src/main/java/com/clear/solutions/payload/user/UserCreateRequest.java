package com.clear.solutions.payload.user;

import com.clear.solutions.validation.constraints.Age;
import com.clear.solutions.validation.constraints.Email;
import com.clear.solutions.validation.constraints.FirstName;
import com.clear.solutions.validation.constraints.LastName;
import com.clear.solutions.validation.constraints.Phone;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserCreateRequest {

  @NotNull
  @Email
  private String email;

  @NotNull
  @FirstName
  private String firstName;

  @NotNull
  @LastName
  private String lastName;

  @NotNull
  @Age
  private LocalDate birthDate;

  @Nullable
  private String address;

  @Nullable
  @Phone
  private String phoneNumber;
}
