package com.pisethjava.facade.booking.subsystem;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    public String pay(String token, BigDecimal amount) {
        if ("FAIL".equalsIgnoreCase(token)) {
            throw new IllegalArgumentException("Payment failed");
        }
        return "PAY-" + Math.abs(token.hashCode());
    }
}
