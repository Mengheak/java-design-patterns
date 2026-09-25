package com.pisethjava.chain.demo;

import com.pisethjava.chain.payment.*;

import java.math.BigDecimal;
import java.util.Set;

public final class PaymentChainFailureDemo {

    private PaymentChainFailureDemo() {
    }

    public static void run() {
        System.out.println("\n=== STOP CHAIN ON FAILURE ===");

        PaymentHandler validation = new ValidationHandler();
        PaymentHandler authentication = new AuthenticationHandler();
        PaymentHandler authorization = new AuthorizationHandler();
        PaymentHandler processing = new ProcessPaymentHandler();

        validation.setNext(authentication);
        authentication.setNext(authorization);
        authorization.setNext(processing);

        PaymentContext context = new PaymentContext(
                new PaymentRequest(new BigDecimal("20.00"), "USD"),
                "USER-2",
                Set.of(),
                1
        );

        try {
            validation.handle(context);
        } catch (RuntimeException exception) {
            System.out.println("Chain stopped: " + exception.getMessage());
        }

        System.out.println("Processed = " + context.processed());
    }
}
