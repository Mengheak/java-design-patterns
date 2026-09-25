package com.pisethjava.template.lesson04_strategy_vs_template;

import java.math.BigDecimal;

public abstract class PaymentTemplate {
	public final void process(BigDecimal amount) {
		validate(amount);
		fraudCheck(amount);
		charge(amount);
		sendReceipt();
		saveTransaction();
	}

	private void validate(BigDecimal amount) {
		if (amount == null || amount.signum() <= 0)
			throw new IllegalArgumentException("Amount must be greater than zero");
		System.out.println("Template: Validate payment");
	}

	protected abstract void fraudCheck(BigDecimal amount);

	protected abstract void charge(BigDecimal amount);

	private void sendReceipt() {
		System.out.println("Template: Send receipt");
	}

	private void saveTransaction() {
		System.out.println("Template: Save transaction");
	}
}
