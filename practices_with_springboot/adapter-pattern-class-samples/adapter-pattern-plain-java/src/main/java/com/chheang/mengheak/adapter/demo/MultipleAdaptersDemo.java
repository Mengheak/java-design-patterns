package com.chheang.mengheak.adapter.demo;

import com.chheang.mengheak.adapter.payment.adapter.*;
import com.chheang.mengheak.adapter.payment.domain.*;
import com.chheang.mengheak.adapter.payment.external.aba.*;
import com.chheang.mengheak.adapter.payment.external.stripe.*;
import com.chheang.mengheak.adapter.payment.external.wing.*;
import com.chheang.mengheak.adapter.payment.service.*;
import com.chheang.mengheak.adapter.payment.target.*;
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
