package com.pisethjava.chain.demo;

import com.pisethjava.chain.payment.*;

import java.math.BigDecimal;
import java.util.Set;

public final class PaymentChainDemo {

    private PaymentChainDemo() {
    }

    public static void run() {
        System.out.println("\n=== PAYMENT PIPELINE ===");

        PaymentHandler validation = new ValidationHandler();
        PaymentHandler authentication = new AuthenticationHandler();
        PaymentHandler authorization = new AuthorizationHandler();
        PaymentHandler rateLimit = new RateLimitHandler();
        PaymentHandler fraud = new FraudHandler();
        PaymentHandler processing = new ProcessPaymentHandler();

        validation.setNext(authentication);
        authentication.setNext(authorization);
        authorization.setNext(rateLimit);
        rateLimit.setNext(fraud);
        fraud.setNext(processing);

        PaymentContext context = new PaymentContext(
                new PaymentRequest(new BigDecimal("150.00"), "USD"),
                "USER-1",
                Set.of("PAYMENT_CREATE"),
                10
        );

        validation.handle(context);
        System.out.println("Processed = " + context.processed());
    }
}
