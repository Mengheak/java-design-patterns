package com.pisethjava.adapter.gateway.adapter;

import com.pisethjava.adapter.domain.*;
import com.pisethjava.adapter.external.stripe.*;
import com.pisethjava.adapter.gateway.*;
import org.springframework.stereotype.*;
import java.math.RoundingMode;

@Component
public class StripePaymentAdapter implements PaymentGateway {
	private final StripeSdk sdk;

	public StripePaymentAdapter(StripeSdk s) {
		sdk = s;
	}

	public PaymentProvider provider() {
		return PaymentProvider.STRIPE;
	}

	public PaymentResult pay(PaymentCommand c) {
		long cents = c.amount().movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact();
		StripeResponse r = sdk.charge(cents, c.currency().toLowerCase(), c.account());
		return new PaymentResult(provider(), r.chargeId(), r.paid() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED,
				r.paid() ? "Paid by Stripe" : r.failureMessage());
	}
}
