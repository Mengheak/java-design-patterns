package com.pisethjava.adapter.service;

import com.pisethjava.adapter.domain.*;
import com.pisethjava.adapter.gateway.*;
import org.springframework.stereotype.*;
import java.util.*;

@Service
public class PaymentService {
	private final PaymentGatewayRegistry registry;

	public PaymentService(PaymentGatewayRegistry r) {
		registry = r;
	}

	public PaymentResult pay(PaymentProvider p, PaymentCommand c) {
		return registry.get(p).pay(c);
	}

	public List<PaymentProvider> providers() {
		return registry.supported();
	}
}
