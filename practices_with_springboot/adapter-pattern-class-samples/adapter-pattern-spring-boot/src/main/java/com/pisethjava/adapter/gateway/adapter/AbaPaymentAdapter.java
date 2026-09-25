package com.pisethjava.adapter.gateway.adapter;

import com.pisethjava.adapter.domain.*;
import com.pisethjava.adapter.external.aba.*;
import com.pisethjava.adapter.gateway.*;
import org.springframework.stereotype.*;

@Component
public class AbaPaymentAdapter implements PaymentGateway {
	private final AbaSdk sdk;

	public AbaPaymentAdapter(AbaSdk s) {
		sdk = s;
	}

	public PaymentProvider provider() {
		return PaymentProvider.ABA;
	}

	public PaymentResult pay(PaymentCommand c) {
		AbaResponse r = sdk.submit(c.account(), c.amount().doubleValue(), c.currency(), c.reference());
		PaymentStatus s = switch (r.status()) {
		case APPROVED -> PaymentStatus.SUCCESS;
		case DECLINED -> PaymentStatus.FAILED;
		case PROCESSING -> PaymentStatus.PENDING;
		};
		return new PaymentResult(provider(), r.transactionCode(), s, r.description());
	}
}
