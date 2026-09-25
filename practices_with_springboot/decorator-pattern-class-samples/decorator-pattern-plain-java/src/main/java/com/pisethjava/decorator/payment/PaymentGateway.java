package com.pisethjava.decorator.payment;

public interface PaymentGateway {

    PaymentResult pay(PaymentRequest request);
}
