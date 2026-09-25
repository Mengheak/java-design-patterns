package com.pisethjava.chain.before;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        String currency
) {
}
