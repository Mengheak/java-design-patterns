package com.pisethjava.facade.booking.subsystem;

public final class BookingNotificationService {
    public void sendConfirmation(String g,String b) {
        System.out.println("Notify "+g+" for "+b);
    }
}
