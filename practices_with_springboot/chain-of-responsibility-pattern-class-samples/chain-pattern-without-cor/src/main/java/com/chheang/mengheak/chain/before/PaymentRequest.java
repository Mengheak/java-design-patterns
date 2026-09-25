package com.chheang.mengheak.chain.before;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        String currency
) {
}
