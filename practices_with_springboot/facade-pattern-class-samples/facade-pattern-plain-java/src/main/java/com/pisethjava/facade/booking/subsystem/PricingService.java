package com.pisethjava.facade.booking.subsystem;

import com.pisethjava.facade.booking.domain.*;
import java.math.*;
import java.time.temporal.*;

public final class PricingService {
    public BigDecimal calculate(BookingRequest r) {
        long nights=ChronoUnit.DAYS.between(r.checkIn(),r.checkOut());
        if(nights<=0)throw new IllegalArgumentException("Invalid dates");
        var total=BigDecimal.valueOf(nights).multiply(new BigDecimal("50.00"));
        System.out.println("Price: "+total);
        return total;
    }
}
