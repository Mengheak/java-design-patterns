package com.pisethjava.decorator.payment;

public record AbaResponse(
        String transactionCode,
        boolean approved
) {
}
