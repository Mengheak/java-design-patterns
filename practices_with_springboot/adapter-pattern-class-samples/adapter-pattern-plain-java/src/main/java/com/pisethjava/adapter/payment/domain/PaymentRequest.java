package com.pisethjava.adapter.payment.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record PaymentRequest(
		String account, 
		BigDecimal amount, 
		String currency, 
		String reference) {
	public PaymentRequest {
		Objects.requireNonNull(account);
		Objects.requireNonNull(amount);
		Objects.requireNonNull(currency);
		Objects.requireNonNull(reference);
		if (account.isBlank()) {
			throw new IllegalArgumentException("account required");
		}

		if (amount.signum() <= 0) {
			throw new IllegalArgumentException("amount must be positive");
		}

	}
}
