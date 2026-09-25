package com.pisethjava.facade.booking.api;

import com.pisethjava.facade.booking.domain.BookingCommand;
import com.pisethjava.facade.booking.domain.BookingResult;
import com.pisethjava.facade.booking.facade.BookingFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingFacade bookingFacade;

    public BookingController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse book(@Valid @RequestBody CreateBookingRequest request) {
        BookingResult result = bookingFacade.book(new BookingCommand(
                request.guestId(),
                request.roomId(),
                request.checkIn(),
                request.checkOut(),
                request.paymentToken()
        ));

        return new BookingResponse(
                result.bookingId(),
                result.reservationId(),
                result.paymentTransactionId(),
                result.totalPrice(),
                result.status()
        );
    }
}
