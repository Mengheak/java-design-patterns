package com.pisethjava.adapter.payment.external.wing;

import java.math.BigDecimal;

public class WingMoneyClient {
	public WingReply transfer(String phone, BigDecimal amount, String note) {
		System.out.printf("Wing: %s %s %s%n", phone, amount, note);
		return new WingReply(0, "WING-" + note, "Transfer completed");
	}
}
