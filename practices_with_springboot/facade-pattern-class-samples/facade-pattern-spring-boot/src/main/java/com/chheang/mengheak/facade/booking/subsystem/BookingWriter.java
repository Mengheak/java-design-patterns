package com.chheang.mengheak.facade.booking.subsystem;

import com.chheang.mengheak.facade.booking.domain.BookingCommand;
import org.springframework.stereotype.Service;

@Service
public class BookingWriter {
    public String create(BookingCommand command) {
        return "BOOK-" + command.roomId();
    }
}
