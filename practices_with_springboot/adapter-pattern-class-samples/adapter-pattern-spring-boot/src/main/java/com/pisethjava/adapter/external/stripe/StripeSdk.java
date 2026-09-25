package com.pisethjava.adapter.external.stripe;

public class StripeSdk {
	public StripeResponse charge(long c, String u, String t) {
		return new StripeResponse("ch_" + Math.abs(t.hashCode()), true, null);
	}
}
