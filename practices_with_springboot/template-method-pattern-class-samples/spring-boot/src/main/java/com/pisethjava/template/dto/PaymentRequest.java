package com.pisethjava.template.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
	@NotBlank
	private String method;
	@NotBlank
	private String accountNo;
	@NotNull
	@DecimalMin("0.01")
	private BigDecimal amount;
}
