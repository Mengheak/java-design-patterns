package com.pisethjava.chain.demo;

import com.pisethjava.chain.validation.*;

import java.util.ArrayList;
import java.util.List;

public final class CollectAllValidationDemo {

    private CollectAllValidationDemo() {
    }

    public static void run() {
        System.out.println("\n=== COLLECT ALL VALIDATION ERRORS ===");

        List<RegistrationValidator> validators = List.of(
                new EmailValidator(),
                new PasswordValidator(),
                new PhoneValidator()
        );

        RegistrationRequest request = new RegistrationRequest(
                "wrong-email",
                "123",
                ""
        );

        List<ValidationError> errors = new ArrayList<>();

        for (RegistrationValidator validator : validators) {
            validator.validate(request).ifPresent(errors::add);
        }

        errors.forEach(error ->
                System.out.println(error.field() + ": " + error.message())
        );
    }
}
