package com.pisethjava.template.lesson04_strategy_vs_template;

import java.math.BigDecimal;

public class AbaPaymentStrategy implements PaymentStrategy {
	public void pay(BigDecimal amount) {
		System.out.println("Strategy: Pay by ABA " + amount);
	}
}
