package com.pisethjava.facade.booking.subsystem;

import org.springframework.stereotype.Service;

@Service
public class AuditService {
    public void record(String bookingId) {
        System.out.println("Audit booking: " + bookingId);
    }
}
