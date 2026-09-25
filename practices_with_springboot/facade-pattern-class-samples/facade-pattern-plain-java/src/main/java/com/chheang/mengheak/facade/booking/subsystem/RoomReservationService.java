package com.chheang.mengheak.facade.booking.subsystem;

import com.chheang.mengheak.facade.booking.domain.*;

public final class RoomReservationService {
    public Reservation reserve(BookingRequest r) {
        var x=new Reservation("RES-"+r.roomId());
        System.out.println("Reserve: "+x.id());
        return x;
    }
    public void release(String id) {
        System.out.println("COMPENSATE release "+id);
    }
}
