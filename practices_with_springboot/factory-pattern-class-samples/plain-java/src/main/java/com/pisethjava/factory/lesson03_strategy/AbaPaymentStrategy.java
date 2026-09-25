package com.pisethjava.factory.lesson03_strategy;

import java.math.BigDecimal;

public class AbaPaymentStrategy implements PaymentStrategy {
	@Override
	public void pay(BigDecimal amount) {
		System.out.println("Pay by ABA: $" + amount);
	}
}
