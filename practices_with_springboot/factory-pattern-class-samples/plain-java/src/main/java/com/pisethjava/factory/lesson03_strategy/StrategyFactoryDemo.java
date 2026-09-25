package com.pisethjava.factory.lesson03_strategy;

import java.math.BigDecimal;

public class StrategyFactoryDemo {
	public static void main(String[] args) {
		PaymentService service = new PaymentService(new PaymentStrategyFactory());
		service.pay("ABA", BigDecimal.valueOf(25));
		service.pay("ACLEDA", BigDecimal.valueOf(40));
	}
}
