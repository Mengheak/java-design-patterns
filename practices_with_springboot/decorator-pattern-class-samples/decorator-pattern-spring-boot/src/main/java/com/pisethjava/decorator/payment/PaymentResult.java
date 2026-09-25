package com.pisethjava.decorator.payment;

public record PaymentResult(
        String transactionId,
        boolean successful,
        String provider
) {
}
