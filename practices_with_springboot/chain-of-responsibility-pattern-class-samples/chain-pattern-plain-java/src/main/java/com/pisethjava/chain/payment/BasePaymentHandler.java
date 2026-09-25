package com.pisethjava.chain.payment;

public abstract class BasePaymentHandler implements PaymentHandler {

    private PaymentHandler next;

    @Override
    public void setNext(PaymentHandler next) {
        this.next = next;
    }

    protected void next(PaymentContext context) {
        if (next != null) {
            next.handle(context);
        }
    }
}
