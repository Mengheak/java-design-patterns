package com.pisethjava.factoryspring.payment;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component("KHQR")
public class KhqrPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Spring: Pay by KHQR = $" + amount);
    }
}
