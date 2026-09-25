package com.pisethjava.chain.validation;

import java.util.Optional;

public final class PasswordValidator implements RegistrationValidator {

    @Override
    public Optional<ValidationError> validate(RegistrationRequest request) {
        if (request.password() == null || request.password().length() < 8) {
            return Optional.of(new ValidationError("password", "Password must contain at least 8 characters"));
        }

        return Optional.empty();
    }
}
