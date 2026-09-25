package com.pisethjava.facade.booking.subsystem;

import java.time.*;

public final class RoomAvailabilityService {
    public void check(String room,LocalDate in,LocalDate out) {
        System.out.println("Check room: "+room);
    }
}
