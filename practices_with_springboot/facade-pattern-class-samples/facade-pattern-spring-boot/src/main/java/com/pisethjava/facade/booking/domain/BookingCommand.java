package com.pisethjava.facade.booking.domain;

import java.time.LocalDate;

public record BookingCommand(
        String guestId,
        String roomId,
        LocalDate checkIn,
        LocalDate checkOut,
        String paymentToken
) {
}
