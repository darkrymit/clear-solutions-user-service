package com.clear.solutions.user.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsByEmailException extends RuntimeException {

    private final String email;

    public UserAlreadyExistsByEmailException(String email) {
        super("Email already registered: " + email);
        this.email = email;
    }

}
