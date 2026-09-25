package com.pisethjava.template.domain;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class PaymentTransaction {
	UUID id;
	String method;
	String accountNo;
	BigDecimal amount;
	String status;
	LocalDateTime createdAt;
}
