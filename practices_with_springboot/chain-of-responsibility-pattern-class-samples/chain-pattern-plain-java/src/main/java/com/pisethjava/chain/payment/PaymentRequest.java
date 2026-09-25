package com.pisethjava.chain.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        String currency
) {
}
