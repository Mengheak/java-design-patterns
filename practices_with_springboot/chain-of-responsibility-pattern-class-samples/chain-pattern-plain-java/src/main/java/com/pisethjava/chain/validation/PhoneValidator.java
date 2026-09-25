package com.pisethjava.chain.validation;

import java.util.Optional;

public final class PhoneValidator implements RegistrationValidator {

    @Override
    public Optional<ValidationError> validate(RegistrationRequest request) {
        if (request.phone() == null || request.phone().isBlank()) {
            return Optional.of(new ValidationError("phone", "Phone is required"));
        }

        return Optional.empty();
    }
}
