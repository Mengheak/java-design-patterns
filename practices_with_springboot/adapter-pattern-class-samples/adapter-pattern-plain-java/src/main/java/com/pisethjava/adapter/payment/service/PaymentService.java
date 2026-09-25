package com.pisethjava.adapter.payment.service;

import com.pisethjava.adapter.payment.domain.*;
import com.pisethjava.adapter.payment.target.PaymentGateway;

public final class PaymentService {
	private final PaymentGateway gateway;

	public PaymentService(PaymentGateway gateway) {
		this.gateway = gateway;
	}

	public PaymentResult checkout(PaymentRequest request) {
		System.out.println("PaymentService processing " + request.reference());
		return gateway.pay(request);
	}
}
