package com.pisethjava.chain.payment;

import java.math.BigDecimal;

public final class FraudHandler extends BasePaymentHandler {

    private static final BigDecimal MANUAL_REVIEW_THRESHOLD = new BigDecimal("10000");

    @Override
    public void handle(PaymentContext context) {
        if (context.request().amount().compareTo(MANUAL_REVIEW_THRESHOLD) > 0) {
            throw new IllegalStateException("Payment requires manual review");
        }

        System.out.println("5. Fraud check passed");
        next(context);
    }
}
