package com.chheang.mengheak.adapter.external.stripe;

public record StripeResponse(String chargeId, boolean paid, String failureMessage) {
}
