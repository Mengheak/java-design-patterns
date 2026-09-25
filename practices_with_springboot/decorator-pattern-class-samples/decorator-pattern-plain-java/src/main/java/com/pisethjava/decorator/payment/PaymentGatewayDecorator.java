package com.pisethjava.decorator.payment;

import java.util.Objects;

public abstract class PaymentGatewayDecorator implements PaymentGateway {

    protected final PaymentGateway delegate;

    protected PaymentGatewayDecorator(PaymentGateway delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate is required");
    }
}
