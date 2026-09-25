package com.pisethjava.facade.booking.facade;

import com.pisethjava.facade.booking.domain.BookingCommand;
import com.pisethjava.facade.booking.domain.BookingResult;
import com.pisethjava.facade.booking.subsystem.AuditService;
import com.pisethjava.facade.booking.subsystem.BookingWriter;
import com.pisethjava.facade.booking.subsystem.GuestService;
import com.pisethjava.facade.booking.subsystem.LoyaltyService;
import com.pisethjava.facade.booking.subsystem.NotificationService;
import com.pisethjava.facade.booking.subsystem.PaymentService;
import com.pisethjava.facade.booking.subsystem.PricingService;
import com.pisethjava.facade.booking.subsystem.RoomAvailabilityService;
import com.pisethjava.facade.booking.subsystem.RoomReservationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BookingFacade {

    private final GuestService guestService;
    private final RoomAvailabilityService availabilityService;
    private final PricingService pricingService;
    private final RoomReservationService reservationService;
    private final PaymentService paymentService;
    private final BookingWriter bookingWriter;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final LoyaltyService loyaltyService;

    public BookingFacade(
            GuestService guestService,
            RoomAvailabilityService availabilityService,
            PricingService pricingService,
            RoomReservationService reservationService,
            PaymentService paymentService,
            BookingWriter bookingWriter,
            NotificationService notificationService,
            AuditService auditService,
            LoyaltyService loyaltyService
    ) {
        this.guestService = guestService;
        this.availabilityService = availabilityService;
        this.pricingService = pricingService;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.bookingWriter = bookingWriter;
        this.notificationService = notificationService;
        this.auditService = auditService;
        this.loyaltyService = loyaltyService;
    }

    public BookingResult book(BookingCommand command) {
        guestService.validate(command.guestId());
        availabilityService.check(
                command.roomId(),
                command.checkIn(),
                command.checkOut()
        );

        BigDecimal totalPrice = pricingService.calculate(command);
        String reservationId = reservationService.reserve(command);
        String transactionId = paymentService.pay(
                command.paymentToken(),
                totalPrice
        );
        String bookingId = bookingWriter.create(command);

        notificationService.sendConfirmation(command.guestId(), bookingId);
        auditService.record(bookingId);
        loyaltyService.award(command.guestId(), totalPrice);

        return new BookingResult(
                bookingId,
                reservationId,
                transactionId,
                totalPrice,
                "CONFIRMED"
        );
    }
}
