package com.pisethjava.chain.payment;

public final class RateLimitHandler extends BasePaymentHandler {

    @Override
    public void handle(PaymentContext context) {
        if (context.requestsToday() >= 100) {
            throw new IllegalStateException("Daily payment request limit reached");
        }

        System.out.println("4. Rate limit passed");
        next(context);
    }
}
