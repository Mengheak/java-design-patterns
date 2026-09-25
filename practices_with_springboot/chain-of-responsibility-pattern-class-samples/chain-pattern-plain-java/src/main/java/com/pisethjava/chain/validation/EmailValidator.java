package com.pisethjava.chain.validation;

import java.util.Optional;

public final class EmailValidator implements RegistrationValidator {

    @Override
    public Optional<ValidationError> validate(RegistrationRequest request) {
        if (request.email() == null || !request.email().contains("@")) {
            return Optional.of(new ValidationError("email", "Email is invalid"));
        }

        return Optional.empty();
    }
}
