package com.pisethjava.decorator.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        String account,
        BigDecimal amount,
        String currency,
        String reference
) {
}
