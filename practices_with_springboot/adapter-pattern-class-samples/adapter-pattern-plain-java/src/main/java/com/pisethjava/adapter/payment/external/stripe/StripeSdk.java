package com.pisethjava.adapter.payment.external.stripe;

public class StripeSdk {
	public StripeCharge createCharge(long cents, String currency, String token) {
		System.out.printf("Stripe SDK: %d %s %s%n", cents, currency, token);
		return new StripeCharge("ch_" + Math.abs(token.hashCode()), true, null);
	}
}
