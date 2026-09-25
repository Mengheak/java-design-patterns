package com.pisethjava.template.template;

import com.pisethjava.template.domain.PaymentTransaction;
import com.pisethjava.template.dto.PaymentRequest;
import org.springframework.stereotype.Component;

@Component("ABA")
public class AbaPaymentProcessor extends PaymentProcessingTemplate {
	protected void fraudCheck(PaymentRequest request) {
		System.out.println("ABA fraud check");
	}

	protected PaymentTransaction chargePayment(PaymentRequest request) {
		System.out.println("Charge by ABA: " + request.getAmount());
		return createSuccessTransaction(request);
	}
}
