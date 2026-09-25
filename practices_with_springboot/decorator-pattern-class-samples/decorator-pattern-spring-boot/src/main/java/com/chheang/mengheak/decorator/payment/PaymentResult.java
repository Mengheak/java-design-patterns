package com.chheang.mengheak.decorator.payment;

public record PaymentResult(
        String transactionId,
        boolean successful,
        String provider
) {
}
