package com.chheang.mengheak.adapter.gateway;

import com.chheang.mengheak.adapter.domain.PaymentProvider;

public class UnsupportedPaymentProviderException extends RuntimeException {
	public UnsupportedPaymentProviderException(PaymentProvider p) {
		super("Unsupported provider: " + p);
	}
}
