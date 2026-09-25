package com.chheang.mengheak.adapter.service;

import com.chheang.mengheak.adapter.domain.*;
import com.chheang.mengheak.adapter.gateway.*;
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
