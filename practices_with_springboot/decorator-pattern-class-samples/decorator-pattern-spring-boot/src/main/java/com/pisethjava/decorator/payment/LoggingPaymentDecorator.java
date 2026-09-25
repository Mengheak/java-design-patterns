package com.pisethjava.decorator.payment;

public final class LoggingPaymentDecorator extends PaymentGatewayDecorator {

    public LoggingPaymentDecorator(PaymentGateway delegate) {
        super(delegate);
    }

    @Override
    public PaymentResult pay(PaymentRequest request) {
        System.out.println("[LOG] payment started: " + request.reference());
        PaymentResult result = delegate.pay(request);
        System.out.println("[LOG] payment finished: " + result.transactionId());
        return result;
    }
}
