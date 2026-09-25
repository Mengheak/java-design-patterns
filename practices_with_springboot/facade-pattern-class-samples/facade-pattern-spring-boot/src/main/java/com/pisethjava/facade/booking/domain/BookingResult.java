package com.pisethjava.facade.booking.domain;

import java.math.BigDecimal;

public record BookingResult(
        String bookingId,
        String reservationId,
        String paymentTransactionId,
        BigDecimal totalPrice,
        String status
) {
}
