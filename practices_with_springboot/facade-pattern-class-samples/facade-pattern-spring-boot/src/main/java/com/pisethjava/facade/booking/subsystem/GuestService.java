package com.pisethjava.facade.booking.subsystem;

import org.springframework.stereotype.Service;

@Service
public class GuestService {
    public void validate(String guestId) {
        if (guestId == null || guestId.isBlank()) {
            throw new IllegalArgumentException("Guest ID is required");
        }
    }
}
