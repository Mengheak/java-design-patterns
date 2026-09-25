package com.chheang.mengheak.adapter.external.aba;

public record AbaResponse(String transactionCode, AbaStatus status, String description) {
}
