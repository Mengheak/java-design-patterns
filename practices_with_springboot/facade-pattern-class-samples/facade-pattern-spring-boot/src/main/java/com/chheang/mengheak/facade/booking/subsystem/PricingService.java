package com.chheang.mengheak.facade.booking.subsystem;

import com.chheang.mengheak.facade.booking.domain.BookingCommand;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
public class PricingService {
    public BigDecimal calculate(BookingCommand command) {
        long nights = ChronoUnit.DAYS.between(command.checkIn(), command.checkOut());
        return BigDecimal.valueOf(nights).multiply(new BigDecimal("50.00"));
    }
}
