package com.pisethjava.adapter.api;

import com.pisethjava.adapter.domain.*;

public record PaymentResponse(PaymentProvider provider, String transactionId, PaymentStatus status, String message) {
}
