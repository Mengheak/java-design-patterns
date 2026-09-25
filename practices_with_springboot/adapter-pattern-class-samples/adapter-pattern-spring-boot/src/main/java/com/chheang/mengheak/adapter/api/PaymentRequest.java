package com.chheang.mengheak.adapter.api;

import com.chheang.mengheak.adapter.domain.PaymentProvider;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PaymentRequest(@NotNull PaymentProvider provider, @NotBlank String account,
		@NotNull @DecimalMin("0.01") BigDecimal amount, @NotBlank String currency, @NotBlank String reference) {
}
