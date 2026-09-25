package com.chheang.mengheak.facade.booking.subsystem;

import com.chheang.mengheak.facade.booking.domain.*;

public final class BookingWriter {
    private final boolean fail;
    public BookingWriter(boolean f) {
        fail=f;
    }
    public String create(BookingRequest r) {
        if(fail)throw new IllegalStateException("Simulated booking failure");
        var id="BOOK-"+r.roomId();
        System.out.println("Create: "+id);
        return id;
    }
}
