package com.pisethjava.facade.booking.subsystem;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RoomAvailabilityService {
    public void check(String roomId, LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }
    }
}
