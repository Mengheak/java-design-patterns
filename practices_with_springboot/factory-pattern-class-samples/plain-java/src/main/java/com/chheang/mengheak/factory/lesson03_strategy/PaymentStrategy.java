package com.chheang.mengheak.factory.lesson03_strategy;

import java.math.BigDecimal;

public interface PaymentStrategy {
    void pay(BigDecimal amount);
}
