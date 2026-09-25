package com.pisethjava.adapter.gateway.adapter;

import com.pisethjava.adapter.domain.*;
import com.pisethjava.adapter.external.wing.*;
import com.pisethjava.adapter.gateway.*;
import org.springframework.stereotype.*;

@Component
public class WingPaymentAdapter implements PaymentGateway {
	private final WingClient client;

	public WingPaymentAdapter(WingClient c) {
		client = c;
	}

	public PaymentProvider provider() {
		return PaymentProvider.WING;
	}

	public PaymentResult pay(PaymentCommand c) {
		WingResponse r = client.transfer(c.account(), c.amount(), c.reference());
		return new PaymentResult(provider(), r.transferId(),
				r.responseCode() == 0 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED, r.message());
	}
}
