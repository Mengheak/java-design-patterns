package com.pisethjava.factory.lesson03_strategy;

public class PaymentStrategyFactory {
	public PaymentStrategy getStrategy(String method) {
		if ("ABA".equalsIgnoreCase(method))
			return new AbaPaymentStrategy();
		if ("ACLEDA".equalsIgnoreCase(method))
			return new AcledaPaymentStrategy();
		throw new IllegalArgumentException("Unsupported payment method: " + method);
	}
}
