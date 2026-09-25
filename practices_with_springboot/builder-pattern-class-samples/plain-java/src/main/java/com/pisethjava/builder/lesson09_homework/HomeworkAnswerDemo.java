package com.pisethjava.builder.lesson09_homework;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class HomeworkAnswerDemo {
    public static void main(String[] args) {
        Reservation reservation = Reservation.builder()
                .reservationNo("RES-2026-0001")
                .guest(Guest.builder()
                        .id(UUID.randomUUID())
                        .firstName("John")
                        .lastName("Smith")
                        .phone("012345678")
                        .email("john@example.com")
                        .nationality("USA")
                        .passportNumber("P123456")
                        .vip(true)
                        .build())
                .room(Room.builder()
                        .roomCode("DLX001")
                        .roomType("DELUXE")
                        .price(BigDecimal.valueOf(80))
                        .currency("USD")
                        .maxAdult(2)
                        .maxChild(1)
                        .wifi(true)
                        .balcony(true)
                        .build())
                .checkIn(LocalDate.of(2026, 7, 10))
                .checkOut(LocalDate.of(2026, 7, 12))
                .numberOfGuests(2)
                .specialRequest("High floor room")
                .status("CONFIRMED")
                .build();

        System.out.println(reservation);
    }
}
