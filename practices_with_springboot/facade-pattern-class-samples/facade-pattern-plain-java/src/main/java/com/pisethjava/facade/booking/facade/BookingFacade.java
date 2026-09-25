package com.pisethjava.facade.booking.facade;

import com.pisethjava.facade.booking.domain.*;
import com.pisethjava.facade.booking.subsystem.*;

public class BookingFacade {
    protected final GuestService g;
    protected final RoomAvailabilityService a;
    protected final PricingService p;
    protected final RoomReservationService r;
    protected final BookingPaymentService pay;
    protected final BookingWriter w;
    protected final BookingNotificationService n;
    protected final BookingAuditService audit;
    protected final LoyaltyService l;
    public BookingFacade(GuestService g,RoomAvailabilityService a,PricingService p,RoomReservationService r,BookingPaymentService pay,BookingWriter w,BookingNotificationService n,BookingAuditService audit,LoyaltyService l) {
        this.g=g;
        this.a=a;
        this.p=p;
        this.r=r;
        this.pay=pay;
        this.w=w;
        this.n=n;
        this.audit=audit;
        this.l=l;
    }
    public BookingResult book(BookingRequest x) {
        g.validate(x.guestId());
        a.check(x.roomId(),x.checkIn(),x.checkOut());
        var total=p.calculate(x);
        var res=r.reserve(x);
        var payment=pay.pay(x.paymentToken(),total);
        var id=w.create(x);
        n.sendConfirmation(x.guestId(),id);
        audit.record(id);
        l.awardPoints(x.guestId(),total);
        return new BookingResult(id,res.id(),payment.transactionId(),total,"CONFIRMED");
    }
}
