package com.pisethjava.chain.before;

import java.math.BigDecimal;

public final class PaymentService {

    private static final int DAILY_REQUEST_LIMIT = 100;

    private static final BigDecimal MANUAL_REVIEW_THRESHOLD =
            new BigDecimal("10000");

    public void pay(PaymentContext context) {
        validateRequest(context);
        authenticate(context);
        authorize(context);
        checkRateLimit(context);
        checkFraud(context);
        processPayment(context);
    }

    private void validateRequest(PaymentContext context) {
        if (context.request() == null) {
            throw new IllegalArgumentException(
                    "Payment request is required"
            );
        }

        if (context.request().amount() == null
                || context.request().amount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }

        System.out.println("1. Validation passed");
    }

    private void authenticate(PaymentContext context) {
        if (context.userId() == null
                || context.userId().isBlank()) {

            throw new SecurityException(
                    "User is not authenticated"
            );
        }

        System.out.println("2. Authentication passed");
    }

    private void authorize(PaymentContext context) {
        if (!context.permissions()
                .contains("PAYMENT_CREATE")) {

            throw new SecurityException(
                    "PAYMENT_CREATE permission is required"
            );
        }

        System.out.println("3. Authorization passed");
    }

    private void checkRateLimit(PaymentContext context) {
        if (context.requestsToday()
                >= DAILY_REQUEST_LIMIT) {

            throw new IllegalStateException(
                    "Daily payment request limit reached"
            );
        }

        System.out.println("4. Rate limit passed");
    }

    private void checkFraud(PaymentContext context) {
        if (context.request().amount()
                .compareTo(MANUAL_REVIEW_THRESHOLD) > 0) {

            throw new IllegalStateException(
                    "Payment requires manual review"
            );
        }

        System.out.println("5. Fraud check passed");
    }

    private void processPayment(PaymentContext context) {
        System.out.println(
                "6. Process payment: "
                        + context.request().amount()
                        + " "
                        + context.request().currency()
        );

        context.markProcessed();
    }
}
