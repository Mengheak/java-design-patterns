package com.pisethjava.adapter.gateway;

import com.pisethjava.adapter.domain.PaymentProvider;
import org.springframework.stereotype.*;
import java.util.*;

@Component
public class PaymentGatewayRegistry {
	private final Map<PaymentProvider, PaymentGateway> gateways;

	public PaymentGatewayRegistry(List<PaymentGateway> list) {
		Map<PaymentProvider, PaymentGateway> m = new EnumMap<>(PaymentProvider.class);
		for (PaymentGateway g : list) {
			if (m.put(g.provider(), g) != null)
				throw new IllegalStateException("Duplicate " + g.provider());
		}
		gateways = Map.copyOf(m);
	}

	public PaymentGateway get(PaymentProvider p) {
		PaymentGateway g = gateways.get(p);
		if (g == null)
			throw new UnsupportedPaymentProviderException(p);
		return g;
	}

	public List<PaymentProvider> supported() {
		return gateways.keySet().stream().sorted().toList();
	}
}
