package com.chheang.mengheak.template.template;

import com.chheang.mengheak.template.domain.PaymentTransaction;
import com.chheang.mengheak.template.dto.PaymentRequest;
import org.springframework.stereotype.Component;

@Component("KHQR")
public class KhqrPaymentProcessor extends PaymentProcessingTemplate {
	protected void fraudCheck(PaymentRequest request) {
		System.out.println("KHQR fraud check");
	}

	protected PaymentTransaction chargePayment(PaymentRequest request) {
		System.out.println("Charge by KHQR: " + request.getAmount());
		return createSuccessTransaction(request);
	}

	@Override
	protected void sendSms(PaymentTransaction transaction) {
		System.out.println("Send KHQR SMS notification");
	}
}
