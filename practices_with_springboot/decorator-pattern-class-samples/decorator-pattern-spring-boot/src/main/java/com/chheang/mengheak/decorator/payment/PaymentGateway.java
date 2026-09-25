package com.chheang.mengheak.decorator.payment;

public interface PaymentGateway {

    PaymentResult pay(PaymentRequest request);
}
