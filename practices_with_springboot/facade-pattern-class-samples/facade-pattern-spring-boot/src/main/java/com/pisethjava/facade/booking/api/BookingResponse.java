package com.pisethjava.facade.booking.api;

import java.math.*;

public record BookingResponse(String bookingId,String reservationId,String paymentTransactionId,BigDecimal totalPrice,String status) {
}
