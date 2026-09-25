package com.pisethjava.adapter.external.stripe;

public record StripeResponse(String chargeId, boolean paid, String failureMessage) {
}
