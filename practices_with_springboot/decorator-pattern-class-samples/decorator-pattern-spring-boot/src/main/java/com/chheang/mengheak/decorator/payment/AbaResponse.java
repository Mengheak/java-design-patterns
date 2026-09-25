package com.chheang.mengheak.decorator.payment;

public record AbaResponse(
        String transactionCode,
        boolean approved
) {
}
