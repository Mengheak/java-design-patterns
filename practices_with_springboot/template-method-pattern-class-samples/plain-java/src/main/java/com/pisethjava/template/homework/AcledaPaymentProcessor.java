package com.pisethjava.template.homework;

import java.math.BigDecimal;

public class AcledaPaymentProcessor extends PaymentProcessingTemplate {
	protected void fraudCheck(String accountNo, BigDecimal amount) {
		System.out.println("ACLEDA fraud check");
	}

	protected void chargePayment(String accountNo, BigDecimal amount) {
		System.out.println("Charge payment by ACLEDA: " + amount);
	}
}
