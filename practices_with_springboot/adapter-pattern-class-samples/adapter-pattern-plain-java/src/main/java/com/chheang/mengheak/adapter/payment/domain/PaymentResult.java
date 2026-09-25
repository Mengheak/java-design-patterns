package com.chheang.mengheak.adapter.payment.domain;

public record PaymentResult(
		String transactionId, 
		PaymentStatus status, 
		String message) {
}
