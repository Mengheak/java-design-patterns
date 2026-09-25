package com.pisethjava.adapter.payment.adapter;

import com.pisethjava.adapter.payment.domain.*;
import com.pisethjava.adapter.payment.external.stripe.*;
import com.pisethjava.adapter.payment.target.PaymentGateway;
import java.math.RoundingMode;

public final class StripePaymentAdapter implements PaymentGateway {
	private final StripeSdk sdk;

	public StripePaymentAdapter(StripeSdk sdk) {
		this.sdk = sdk;
	}

	public PaymentResult pay(PaymentRequest r) {
		long cents = r.amount().movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact();
		StripeCharge c = sdk.createCharge(cents, r.currency().toLowerCase(), r.account());
		return new PaymentResult(c.id(), c.paid() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED,
				c.paid() ? "Paid by Stripe" : c.failureMessage());
	}
}
