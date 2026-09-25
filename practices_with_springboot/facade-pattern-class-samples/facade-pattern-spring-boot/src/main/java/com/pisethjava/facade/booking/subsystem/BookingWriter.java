package com.pisethjava.facade.booking.subsystem;

import com.pisethjava.facade.booking.domain.BookingCommand;
import org.springframework.stereotype.Service;

@Service
public class BookingWriter {
    public String create(BookingCommand command) {
        return "BOOK-" + command.roomId();
    }
}
