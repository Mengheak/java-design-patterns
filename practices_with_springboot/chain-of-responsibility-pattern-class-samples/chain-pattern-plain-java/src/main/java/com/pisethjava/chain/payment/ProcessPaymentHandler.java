package com.pisethjava.chain.payment;

public final class ProcessPaymentHandler extends BasePaymentHandler {

    @Override
    public void handle(PaymentContext context) {
        System.out.println(
                "6. Process payment: "
                        + context.request().amount()
                        + " "
                        + context.request().currency()
        );
        context.markProcessed();
    }
}
