package com.chheang.mengheak.facade.booking.subsystem;

import com.chheang.mengheak.facade.booking.domain.*;
import java.math.*;

public final class BookingPaymentService {
    public BookingPayment pay(String t,BigDecimal a) {
        var p=new BookingPayment("PAY-"+Math.abs(t.hashCode()),true);
        System.out.println("Pay: "+p.transactionId());
        return p;
    }
    public void refund(String id) {
        System.out.println("COMPENSATE refund "+id);
    }
}
