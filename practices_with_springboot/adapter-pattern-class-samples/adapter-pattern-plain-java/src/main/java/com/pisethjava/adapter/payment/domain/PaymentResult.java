package com.pisethjava.adapter.payment.domain;

public record PaymentResult(
		String transactionId, 
		PaymentStatus status, 
		String message) {
}
