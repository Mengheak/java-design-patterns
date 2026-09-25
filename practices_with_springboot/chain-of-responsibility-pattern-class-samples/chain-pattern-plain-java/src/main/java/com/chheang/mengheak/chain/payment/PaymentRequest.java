package com.chheang.mengheak.chain.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        String currency
) {
}
