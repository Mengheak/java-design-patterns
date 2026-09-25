package com.chheang.mengheak.adapter.payment.adapter;

import com.chheang.mengheak.adapter.payment.domain.*;
import com.chheang.mengheak.adapter.payment.external.wing.*;
import com.chheang.mengheak.adapter.payment.target.PaymentGateway;

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
