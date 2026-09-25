package com.pisethjava.chain.payment;

import java.math.BigDecimal;

public final class ValidationHandler extends BasePaymentHandler {

    @Override
    public void handle(PaymentContext context) {
        if (context.request() == null) {
            throw new IllegalArgumentException("Payment request is required");
        }

        if (context.request().amount() == null
                || context.request().amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        System.out.println("1. Validation passed");
        next(context);
    }
}
