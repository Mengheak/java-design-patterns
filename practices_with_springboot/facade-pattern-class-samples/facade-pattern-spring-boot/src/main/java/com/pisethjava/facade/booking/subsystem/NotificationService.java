package com.pisethjava.facade.booking.subsystem;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendConfirmation(String guestId, String bookingId) {
        System.out.printf("Confirmation sent to %s for %s%n", guestId, bookingId);
    }
}
