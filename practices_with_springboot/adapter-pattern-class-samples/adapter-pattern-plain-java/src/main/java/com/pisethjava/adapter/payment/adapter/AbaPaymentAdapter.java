package com.pisethjava.adapter.payment.adapter;

import com.pisethjava.adapter.payment.domain.*;
import com.pisethjava.adapter.payment.external.aba.*;
import com.pisethjava.adapter.payment.target.PaymentGateway;

public final class AbaPaymentAdapter implements PaymentGateway {
	private final AbaPaymentSdk sdk;

	public AbaPaymentAdapter(AbaPaymentSdk sdk) {
		this.sdk = sdk;
	}

	//@Override
	public PaymentResult pay(PaymentRequest r) {
		AbaResponse x = sdk.submitPayment(r.account(), r.amount().doubleValue(), r.currency(), r.reference());
		PaymentStatus s = switch (x.status()) {
		case APPROVED -> PaymentStatus.SUCCESS;
		case DECLINED -> PaymentStatus.FAILED;
		case PROCESSING -> PaymentStatus.PENDING;
		};
		return new PaymentResult(x.transactionCode(), s, x.description());
	}

	
}
