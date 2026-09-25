package com.pisethjava.facade.booking.subsystem;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoyaltyService {
    public void award(String guestId, BigDecimal amount) {
        System.out.printf("Award loyalty points to %s for %s%n", guestId, amount);
    }
}
