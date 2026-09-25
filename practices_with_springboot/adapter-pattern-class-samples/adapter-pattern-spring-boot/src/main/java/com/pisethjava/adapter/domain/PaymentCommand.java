package com.pisethjava.adapter.domain;

import java.math.BigDecimal;

public record PaymentCommand(String account, BigDecimal amount, String currency, String reference) {
}
