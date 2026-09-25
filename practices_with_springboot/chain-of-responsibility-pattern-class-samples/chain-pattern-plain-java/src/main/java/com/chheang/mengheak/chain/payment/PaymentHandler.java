package com.chheang.mengheak.chain.payment;

public interface PaymentHandler {
    void handle(PaymentContext context);
    void setNext(PaymentHandler next);
}
