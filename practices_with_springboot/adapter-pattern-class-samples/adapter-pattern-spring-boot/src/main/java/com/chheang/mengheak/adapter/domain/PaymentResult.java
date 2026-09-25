package com.chheang.mengheak.adapter.domain;

public record PaymentResult(PaymentProvider provider, String transactionId, PaymentStatus status, String message) {
}
