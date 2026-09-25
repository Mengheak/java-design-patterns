package com.pisethjava.template.homework;

import java.math.BigDecimal;

public class KhqrPaymentProcessor extends PaymentProcessingTemplate {
	protected void fraudCheck(String accountNo, BigDecimal amount) {
		System.out.println("KHQR fraud check");
	}

	protected void chargePayment(String accountNo, BigDecimal amount) {
		System.out.println("Charge payment by KHQR: " + amount);
	}

	@Override
	protected void sendSms(String accountNo, BigDecimal amount) {
		System.out.println("Send KHQR SMS notification");
	}
}
