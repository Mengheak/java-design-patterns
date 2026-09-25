package com.pisethjava.template.homework;

import java.math.BigDecimal;

public abstract class PaymentProcessingTemplate {
	public final void process(String accountNo, BigDecimal amount) {
		validate(accountNo, amount);
		fraudCheck(accountNo, amount);
		chargePayment(accountNo, amount);
		sendReceipt();
		sendSms(accountNo, amount);
		saveTransaction();
	}

	private void validate(String accountNo, BigDecimal amount) {
		if (accountNo == null || accountNo.isBlank())
			throw new IllegalArgumentException("Account number is required");
		if (amount == null || amount.signum() <= 0)
			throw new IllegalArgumentException("Amount must be greater than zero");
		System.out.println("Validate payment request");
	}

	protected abstract void fraudCheck(String accountNo, BigDecimal amount);

	protected abstract void chargePayment(String accountNo, BigDecimal amount);

	private void sendReceipt() {
		System.out.println("Send receipt");
	}

	protected void sendSms(String accountNo, BigDecimal amount) {
	}

	private void saveTransaction() {
		System.out.println("Save transaction");
	}
}
