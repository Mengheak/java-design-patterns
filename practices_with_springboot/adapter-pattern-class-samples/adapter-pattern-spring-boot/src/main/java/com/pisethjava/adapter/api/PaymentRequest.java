package com.pisethjava.adapter.api;

import com.pisethjava.adapter.domain.PaymentProvider;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PaymentRequest(@NotNull PaymentProvider provider, @NotBlank String account,
		@NotNull @DecimalMin("0.01") BigDecimal amount, @NotBlank String currency, @NotBlank String reference) {
}
