package com.pisethjava.template.lesson04_strategy_vs_template;

import java.math.BigDecimal;

public class StrategyVsTemplateDemo {
	public static void main(String[] args) {
		System.out.println("=== Strategy: choose behavior ===");
		PaymentStrategy strategy = new AbaPaymentStrategy();
		strategy.pay(BigDecimal.valueOf(100));
		System.out.println("=== Template Method: fixed workflow, customized steps ===");
		PaymentTemplate template = new AbaPaymentTemplate();
		template.process(BigDecimal.valueOf(100));
	}
}
