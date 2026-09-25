package com.pisethjava.template.homework;

import java.math.BigDecimal;

public class PaymentTemplateHomeworkDemo {
	public static void main(String[] args) {
		PaymentProcessingTemplate processor = new AbaPaymentProcessor();
		processor.process("ABA-001", BigDecimal.valueOf(100));
		System.out.println();
		processor = new AcledaPaymentProcessor();
		processor.process("ACLEDA-001", BigDecimal.valueOf(150));
		System.out.println();
		processor = new KhqrPaymentProcessor();
		processor.process("KHQR-001", BigDecimal.valueOf(25));
	}
}
