package com.pisethjava.factory.lesson03_strategy;

import java.math.BigDecimal;

public class AcledaPaymentStrategy implements PaymentStrategy {
	@Override
	public void pay(BigDecimal amount) {
		System.out.println("Pay by ACLEDA: $" + amount);
	}
}
