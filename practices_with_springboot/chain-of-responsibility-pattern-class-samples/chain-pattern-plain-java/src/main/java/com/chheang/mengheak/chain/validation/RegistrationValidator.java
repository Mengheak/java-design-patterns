package com.chheang.mengheak.chain.validation;

import java.util.Optional;

public interface RegistrationValidator {
    Optional<ValidationError> validate(RegistrationRequest request);
}
