package com.pisethjava.template.dto;

import lombok.*;
import java.math.BigDecimal;

@Value
@Builder
public class PaymentResponse {
	String transactionId;
	String method;
	String accountNo;
	BigDecimal amount;
	String status;
	String message;
}
