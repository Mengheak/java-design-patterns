package com.pisethjava.chain.payment;

public final class AuthenticationHandler extends BasePaymentHandler {

    @Override
    public void handle(PaymentContext context) {
        if (context.userId() == null || context.userId().isBlank()) {
            throw new SecurityException("User is not authenticated");
        }

        System.out.println("2. Authentication passed");
        next(context);
    }
}
