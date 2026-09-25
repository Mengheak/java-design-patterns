package com.chheang.mengheak.adapter.api;

import com.chheang.mengheak.adapter.domain.*;

public record PaymentResponse(PaymentProvider provider, String transactionId, PaymentStatus status, String message) {
}
