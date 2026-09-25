package com.chheang.mengheak.decorator.payment;

public abstract class PaymentGatewayDecorator implements PaymentGateway {

    protected final PaymentGateway delegate;

    protected PaymentGatewayDecorator(PaymentGateway delegate) {
        this.delegate = delegate;
    }
}
