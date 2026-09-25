package com.chheang.mengheak.adapter.gateway.adapter;

import com.chheang.mengheak.adapter.domain.*;
import com.chheang.mengheak.adapter.external.wing.*;
import com.chheang.mengheak.adapter.gateway.*;
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
