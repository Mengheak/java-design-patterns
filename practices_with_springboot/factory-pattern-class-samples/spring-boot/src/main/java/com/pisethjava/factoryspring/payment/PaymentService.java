package com.pisethjava.factoryspring.payment;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
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
