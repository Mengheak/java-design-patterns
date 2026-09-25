package com.chheang.mengheak.adapter.payment.external.aba;

public record AbaResponse(String transactionCode, AbaPaymentStatus status, String description) {
}
