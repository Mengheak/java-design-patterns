package com.chheang.mengheak.solid.s_srp.good;

import com.chheang.mengheak.solid.common.UserRegisterRequest;

public class UserRegisterValidator {

    public void validate(UserRegisterRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
