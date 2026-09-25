package com.pisethjava.adapter.external.aba;

public record AbaResponse(String transactionCode, AbaStatus status, String description) {
}
