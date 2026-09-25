package com.pisethjava.chain.payment;

public final class AuthorizationHandler extends BasePaymentHandler {

    @Override
    public void handle(PaymentContext context) {
        if (!context.permissions().contains("PAYMENT_CREATE")) {
            throw new SecurityException("PAYMENT_CREATE permission is required");
        }

        System.out.println("3. Authorization passed");
        next(context);
    }
}
