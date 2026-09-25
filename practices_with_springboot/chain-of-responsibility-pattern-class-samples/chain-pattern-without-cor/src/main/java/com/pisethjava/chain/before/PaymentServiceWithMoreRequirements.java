package com.pisethjava.chain.before;

import java.math.BigDecimal;
import java.util.Set;

/**
 * This class intentionally demonstrates how the service grows
 * when new requirements keep arriving.
 */
public final class PaymentServiceWithMoreRequirements {

    private static final int DAILY_REQUEST_LIMIT = 100;

    private static final BigDecimal MANUAL_REVIEW_THRESHOLD =
            new BigDecimal("10000");

    private static final BigDecimal TRANSACTION_LIMIT =
            new BigDecimal("5000");

    private static final Set<String> BLOCKED_COUNTRIES =
            Set.of(
                    "BLOCKED_COUNTRY"
            );

    public void pay(
            PaymentContext context,
            String countryCode,
            boolean blacklisted,
            boolean maintenanceMode
    ) {
        validateRequest(context);
        authenticate(context);
        authorize(context);
        checkBlacklist(blacklisted);
        checkRateLimit(context);
        checkTransactionLimit(context);
        checkCountryRestriction(countryCode);
        checkFraud(context);
        checkMaintenanceMode(maintenanceMode);
        processPayment(context);
    }

    private void validateRequest(
            PaymentContext context
    ) {
        if (context.request() == null
                || context.request().amount() == null) {

            throw new IllegalArgumentException(
                    "Invalid payment request"
            );
        }

        System.out.println("1. Validation passed");
    }

    private void authenticate(
            PaymentContext context
    ) {
        if (context.userId() == null
                || context.userId().isBlank()) {

            throw new SecurityException(
                    "User is not authenticated"
            );
        }

        System.out.println("2. Authentication passed");
    }

    private void authorize(
            PaymentContext context
    ) {
        if (!context.permissions()
                .contains("PAYMENT_CREATE")) {

            throw new SecurityException(
                    "User is not authorized"
            );
        }

        System.out.println("3. Authorization passed");
    }

    private void checkBlacklist(
            boolean blacklisted
    ) {
        if (blacklisted) {
            throw new SecurityException(
                    "User is blacklisted"
            );
        }

        System.out.println("4. Blacklist check passed");
    }

    private void checkRateLimit(
            PaymentContext context
    ) {
        if (context.requestsToday()
                >= DAILY_REQUEST_LIMIT) {

            throw new IllegalStateException(
                    "Daily limit reached"
            );
        }

        System.out.println("5. Rate limit passed");
    }

    private void checkTransactionLimit(
            PaymentContext context
    ) {
        if (context.request().amount()
                .compareTo(TRANSACTION_LIMIT) > 0) {

            throw new IllegalStateException(
                    "Transaction limit exceeded"
            );
        }

        System.out.println(
                "6. Transaction limit passed"
        );
    }

    private void checkCountryRestriction(
            String countryCode
    ) {
        if (BLOCKED_COUNTRIES.contains(
                countryCode
        )) {
            throw new IllegalStateException(
                    "Country is restricted"
            );
        }

        System.out.println(
                "7. Country restriction passed"
        );
    }

    private void checkFraud(
            PaymentContext context
    ) {
        if (context.request().amount()
                .compareTo(
                        MANUAL_REVIEW_THRESHOLD
                ) > 0) {

            throw new IllegalStateException(
                    "Manual review required"
            );
        }

        System.out.println("8. Fraud check passed");
    }

    private void checkMaintenanceMode(
            boolean maintenanceMode
    ) {
        if (maintenanceMode) {
            throw new IllegalStateException(
                    "Payment system is under maintenance"
            );
        }

        System.out.println(
                "9. Maintenance check passed"
        );
    }

    private void processPayment(
            PaymentContext context
    ) {
        System.out.println(
                "10. Process payment"
        );

        context.markProcessed();
    }
}
