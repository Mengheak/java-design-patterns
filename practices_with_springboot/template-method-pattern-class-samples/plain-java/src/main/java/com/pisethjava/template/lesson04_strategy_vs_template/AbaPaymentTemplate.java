package com.pisethjava.template.lesson04_strategy_vs_template;

import java.math.BigDecimal;

public class AbaPaymentTemplate extends PaymentTemplate {
	protected void fraudCheck(BigDecimal amount) {
		System.out.println("Template: ABA fraud check");
	}

	protected void charge(BigDecimal amount) {
		System.out.println("Template: Charge ABA " + amount);
	}
}
