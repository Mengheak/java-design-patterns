package com.pisethjava.adapter.payment.external.stripe;

public record StripeCharge(String id, boolean paid, String failureMessage) {
}
