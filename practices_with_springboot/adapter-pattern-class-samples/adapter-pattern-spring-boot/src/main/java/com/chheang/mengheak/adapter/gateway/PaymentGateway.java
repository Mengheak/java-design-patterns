package com.chheang.mengheak.adapter.gateway;

import com.chheang.mengheak.adapter.domain.*;

public interface PaymentGateway {
	PaymentProvider provider();

	PaymentResult pay(PaymentCommand command);
}
