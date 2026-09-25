package com.pisethjava.facade.booking.facade;

import com.pisethjava.facade.booking.domain.*;
import com.pisethjava.facade.booking.subsystem.*;

public final class CompensatingBookingFacade extends BookingFacade {
    public CompensatingBookingFacade(GuestService g,RoomAvailabilityService a,PricingService p,RoomReservationService r,BookingPaymentService pay,BookingWriter w,BookingNotificationService n,BookingAuditService audit,LoyaltyService l) {
        super(g,a,p,r,pay,w,n,audit,l);
    }
    public BookingResult book(BookingRequest x) {
        Reservation res=null;
        BookingPayment payment=null;
        try {
            g.validate(x.guestId());
            a.check(x.roomId(),x.checkIn(),x.checkOut());
            var total=p.calculate(x);
            res=r.reserve(x);
            payment=pay.pay(x.paymentToken(),total);
            var id=w.create(x);
            n.sendConfirmation(x.guestId(),id);
            audit.record(id);
            l.awardPoints(x.guestId(),total);
            return new BookingResult(id,res.id(),payment.transactionId(),total,"CONFIRMED");
        }
        catch(RuntimeException e) {
            if(payment!=null&&payment.successful())pay.refund(payment.transactionId());
            if(res!=null)r.release(res.id());
            throw e;
        }
    }
}
