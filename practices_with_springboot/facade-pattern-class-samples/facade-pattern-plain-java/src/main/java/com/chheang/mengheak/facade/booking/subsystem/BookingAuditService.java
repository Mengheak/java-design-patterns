package com.chheang.mengheak.facade.booking.subsystem;

public final class BookingAuditService {
    public void record(String b) {
        System.out.println("Audit "+b);
    }
}
