package com.pisethjava.adapter.gateway;

import com.pisethjava.adapter.domain.PaymentProvider;

public class UnsupportedPaymentProviderException extends RuntimeException {
	public UnsupportedPaymentProviderException(PaymentProvider p) {
		super("Unsupported provider: " + p);
	}
}
