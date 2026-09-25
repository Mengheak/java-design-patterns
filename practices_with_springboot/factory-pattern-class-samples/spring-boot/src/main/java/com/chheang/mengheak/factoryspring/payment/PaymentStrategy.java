package com.chheang.mengheak.factoryspring.payment;

import java.math.BigDecimal;

public interface PaymentStrategy {
    void pay(BigDecimal amount);
}
