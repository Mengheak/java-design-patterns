package com.chheang.mengheak.adapter.demo;

import java.math.BigDecimal;

import com.chheang.mengheak.adapter.payment.adapter.AbaPaymentAdapter;
import com.chheang.mengheak.adapter.payment.adapter.WingPaymentAdapter;
import com.chheang.mengheak.adapter.payment.domain.PaymentRequest;
import com.chheang.mengheak.adapter.payment.external.aba.AbaPaymentSdk;
import com.chheang.mengheak.adapter.payment.external.wing.WingMoneyClient;
import com.chheang.mengheak.adapter.payment.service.PaymentService;
import com.chheang.mengheak.adapter.payment.target.PaymentGateway;

public final class ObjectAdapterDemo {
	public static void run() {
		System.out.println("\n2. OBJECT ADAPTER");
		//PaymentGateway gateway = new AbaPaymentAdapter(new AbaPaymentSdk());
		PaymentGateway gateway = new WingPaymentAdapter(new WingMoneyClient());
		PaymentService service = new PaymentService(gateway);
		//PaymentService service = new PaymentService(new AbaPaymentAdapter(new AbaPaymentSdk()));
		//var service = new PaymentService(new WingPaymentAdapter(new WingMoneyClient()));
		System.out.println(
				service.checkout(new PaymentRequest("001-123", new BigDecimal("125.50"), "USD", "ORDER-1001")));
	}
}
