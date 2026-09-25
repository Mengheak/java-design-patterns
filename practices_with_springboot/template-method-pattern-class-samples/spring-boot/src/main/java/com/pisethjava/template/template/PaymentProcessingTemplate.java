package com.pisethjava.template.template;

import com.pisethjava.template.domain.PaymentTransaction;
import com.pisethjava.template.dto.*;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class PaymentProcessingTemplate {
	public final PaymentResponse process(PaymentRequest request) {
		validate(request);
		fraudCheck(request);
		PaymentTransaction transaction = chargePayment(request);
		sendReceipt(transaction);
		sendSms(transaction);
		saveTransaction(transaction);
		return toResponse(transaction);
	}

	private void validate(PaymentRequest request) {
		System.out.println("Validate payment request");
	}

	protected abstract void fraudCheck(PaymentRequest request);

	protected PaymentTransaction createSuccessTransaction(PaymentRequest request) {
		return PaymentTransaction.builder().id(UUID.randomUUID()).method(request.getMethod())
				.accountNo(request.getAccountNo()).amount(request.getAmount()).status("SUCCESS")
				.createdAt(LocalDateTime.now()).build();
	}

	protected abstract PaymentTransaction chargePayment(PaymentRequest request);

	private void sendReceipt(PaymentTransaction transaction) {
		System.out.println("Send receipt for " + transaction.getId());
	}

	protected void sendSms(PaymentTransaction transaction) {
	}

	private void saveTransaction(PaymentTransaction transaction) {
		System.out.println("Save transaction " + transaction.getId());
	}

	private PaymentResponse toResponse(PaymentTransaction transaction) {
		return PaymentResponse.builder().transactionId(transaction.getId().toString()).method(transaction.getMethod())
				.accountNo(transaction.getAccountNo()).amount(transaction.getAmount()).status(transaction.getStatus())
				.message("Payment processed successfully").build();
	}
}
