package com.pisethjava.builder.lesson05_immutable;

import java.math.BigDecimal;
import java.util.UUID;

public class ImmutableBuilderDemo {
    public static void main(String[] args) {
        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .amount(BigDecimal.valueOf(50))
                .build();

        System.out.println(payment);
        System.out.println("Payment has final fields and no setters, so it cannot be changed after creation.");
    }
}
