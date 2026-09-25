package com.pisethjava.factory.lesson03_strategy;

import java.math.BigDecimal;

public class PaymentService {
	private final PaymentStrategyFactory factory;

	public PaymentService(PaymentStrategyFactory factory) {
		this.factory = factory;
	}

	public void pay(String method, BigDecimal amount) {
		PaymentStrategy strategy = factory.getStrategy(method);
		strategy.pay(amount);
	}
}
