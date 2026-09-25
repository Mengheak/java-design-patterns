package com.pisethjava.adapter.gateway;

import com.pisethjava.adapter.domain.*;

public interface PaymentGateway {
	PaymentProvider provider();

	PaymentResult pay(PaymentCommand command);
}
