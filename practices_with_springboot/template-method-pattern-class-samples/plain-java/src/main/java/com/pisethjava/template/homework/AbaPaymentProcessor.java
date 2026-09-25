package com.pisethjava.template.homework;

import java.math.BigDecimal;

public class AbaPaymentProcessor extends PaymentProcessingTemplate {
	protected void fraudCheck(String accountNo, BigDecimal amount) {
		System.out.println("ABA fraud check");
	}

	protected void chargePayment(String accountNo, BigDecimal amount) {
		System.out.println("Charge payment by ABA: " + amount);
	}
}
