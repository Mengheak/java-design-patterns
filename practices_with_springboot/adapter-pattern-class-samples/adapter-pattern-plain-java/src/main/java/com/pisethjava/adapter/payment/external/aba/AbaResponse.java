package com.pisethjava.adapter.payment.external.aba;

public record AbaResponse(String transactionCode, AbaPaymentStatus status, String description) {
}
