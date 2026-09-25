package com.chheang.mengheak.facade.booking.facade;

import org.junit.jupiter.api.*;
import java.time.*;
import com.chheang.mengheak.facade.booking.api.*;
import static org.assertj.core.api.Assertions.*;

class BookingFacadeTest {
    @Test void books() {
        var result=new BookingFacade().book(new CreateBookingRequest("G","R",LocalDate.now().plusDays(1),LocalDate.now().plusDays(3),"T"));
        assertThat(result.status()).isEqualTo("CONFIRMED");
        assertThat(result.totalPrice()).isEqualByComparingTo("100.00");
    }
}
