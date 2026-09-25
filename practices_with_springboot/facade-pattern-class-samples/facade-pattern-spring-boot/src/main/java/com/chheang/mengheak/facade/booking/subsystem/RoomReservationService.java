package com.chheang.mengheak.facade.booking.subsystem;

import com.chheang.mengheak.facade.booking.domain.BookingCommand;
import org.springframework.stereotype.Service;

@Service
public class RoomReservationService {
    public String reserve(BookingCommand command) {
        return "RES-" + command.roomId();
    }
}
