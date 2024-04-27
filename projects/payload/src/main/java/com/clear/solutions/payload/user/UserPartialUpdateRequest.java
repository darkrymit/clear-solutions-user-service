package com.clear.solutions.payload.user;

import com.clear.solutions.validation.constraints.Age;
import com.clear.solutions.validation.constraints.Email;
import com.clear.solutions.validation.constraints.FirstName;
import com.clear.solutions.validation.constraints.LastName;
import com.clear.solutions.validation.constraints.Phone;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserPartialUpdateRequest {

  @Email
  @Nullable
  private String email;

  @FirstName
  @Nullable
  private String firstName;

  @LastName
  @Nullable
  private String lastName;

  @Age
  @Nullable
  private LocalDate birthDate;

  @Nullable
  private String address;

  @Nullable
  @Phone
  private String phoneNumber;
}
