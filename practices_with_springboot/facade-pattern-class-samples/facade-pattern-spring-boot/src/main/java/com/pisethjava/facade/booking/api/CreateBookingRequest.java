package com.pisethjava.facade.booking.api;

import jakarta.validation.constraints.*;
import java.time.*;

public record CreateBookingRequest(@NotBlank String guestId,@NotBlank String roomId,@NotNull @Future LocalDate checkIn,@NotNull @Future LocalDate checkOut,@NotBlank String paymentToken) {
}
