package com.pisethjava.template.lesson04_strategy_vs_template;

import java.math.BigDecimal;

public interface PaymentStrategy {
	void pay(BigDecimal amount);
}
