package com.pisethjava.adapter.payment.external.aba;

public class AbaPaymentSdk {
	public AbaResponse submitPayment(String account, double amount, String currency, String reference) {
		System.out.printf("ABA SDK: %s %.2f %s %s%n", account, amount, currency, reference);
		return new AbaResponse("ABA-" + reference, AbaPaymentStatus.APPROVED, "Approved by ABA");
	}
}
