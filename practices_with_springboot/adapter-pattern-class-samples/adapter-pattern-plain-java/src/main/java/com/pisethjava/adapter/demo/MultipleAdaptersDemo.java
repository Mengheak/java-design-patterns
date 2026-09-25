package com.pisethjava.adapter.demo;

import com.pisethjava.adapter.payment.adapter.*;
import com.pisethjava.adapter.payment.domain.*;
import com.pisethjava.adapter.payment.external.aba.*;
import com.pisethjava.adapter.payment.external.stripe.*;
import com.pisethjava.adapter.payment.external.wing.*;
import com.pisethjava.adapter.payment.service.*;
import com.pisethjava.adapter.payment.target.*;
import java.math.BigDecimal;
import java.util.Map;

public final class MultipleAdaptersDemo {
	public static void run() {
		System.out.println("\n3. MULTIPLE ADAPTERS");
		Map<String, PaymentGateway> m = Map.of("ABA", new AbaPaymentAdapter(new AbaPaymentSdk()), "STRIPE",
				new StripePaymentAdapter(new StripeSdk()), "WING", new WingPaymentAdapter(new WingMoneyClient()));
		m.forEach((n, g) -> System.out.println(
				n + " -> " + new PaymentService(g).checkout(new PaymentRequest(n.equals("WING") ? "012345678" : "token",
						new BigDecimal("25.75"), "USD", "ORDER-" + n))));
	}
}
