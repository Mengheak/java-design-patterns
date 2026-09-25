package com.pisethjava.adapter.demo;

public final class IncompatibleInterfacesDemo {
	public static void run() {
		System.out.println(
				"\n1. Client expects PaymentGateway.pay(PaymentRequest), ABA exposes submitPayment(...). Adapter required.");
	}
}
