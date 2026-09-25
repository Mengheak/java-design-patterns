package com.pisethjava.adapter.payment.adapter;

import com.pisethjava.adapter.payment.domain.*;
import com.pisethjava.adapter.payment.external.wing.*;
import com.pisethjava.adapter.payment.target.PaymentGateway;

public final class WingPaymentAdapter implements PaymentGateway {
	private final WingMoneyClient client;

	public WingPaymentAdapter(WingMoneyClient client) {
		this.client = client;
	}

	public PaymentResult pay(PaymentRequest r) {
		WingReply x = client.transfer(r.account(), r.amount(), r.reference());
		return new PaymentResult(x.transactionReference(), x.code() == 0 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED,
				x.text());
	}
}
