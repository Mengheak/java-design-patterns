package com.pisethjava.template.template;

import com.pisethjava.template.domain.PaymentTransaction;
import com.pisethjava.template.dto.PaymentRequest;
import org.springframework.stereotype.Component;

@Component("ACLEDA")
public class AcledaPaymentProcessor extends PaymentProcessingTemplate {
	protected void fraudCheck(PaymentRequest request) {
		System.out.println("ACLEDA fraud check");
	}

	protected PaymentTransaction chargePayment(PaymentRequest request) {
		System.out.println("Charge by ACLEDA: " + request.getAmount());
		return createSuccessTransaction(request);
	}
}
