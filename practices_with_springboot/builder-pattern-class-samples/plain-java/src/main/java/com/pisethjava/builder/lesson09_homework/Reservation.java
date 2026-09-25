package com.pisethjava.builder.lesson09_homework;

import java.time.LocalDate;

public final class Reservation {
    private final String reservationNo;
    private final Guest guest;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final int numberOfGuests;
    private final String specialRequest;
    private final String status;

    private Reservation(Builder builder) {
        this.reservationNo = builder.reservationNo;
        this.guest = builder.guest;
        this.room = builder.room;
        this.checkIn = builder.checkIn;
        this.checkOut = builder.checkOut;
        this.numberOfGuests = builder.numberOfGuests;
        this.specialRequest = builder.specialRequest;
        this.status = builder.status;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String reservationNo;
        private Guest guest;
        private Room room;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private int numberOfGuests;
        private String specialRequest;
        private String status = "DRAFT";

        public Builder reservationNo(String reservationNo) { this.reservationNo = reservationNo; return this; }
        public Builder guest(Guest guest) { this.guest = guest; return this; }
        public Builder room(Room room) { this.room = room; return this; }
        public Builder checkIn(LocalDate checkIn) { this.checkIn = checkIn; return this; }
        public Builder checkOut(LocalDate checkOut) { this.checkOut = checkOut; return this; }
        public Builder numberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; return this; }
        public Builder specialRequest(String specialRequest) { this.specialRequest = specialRequest; return this; }
        public Builder status(String status) { this.status = status; return this; }

        public Reservation build() {
            if (reservationNo == null || reservationNo.isBlank()) throw new IllegalArgumentException("Reservation number is required");
            if (guest == null) throw new IllegalArgumentException("Guest is required");
            if (room == null) throw new IllegalArgumentException("Room is required");
            if (checkIn == null || checkOut == null) throw new IllegalArgumentException("Check-in and check-out are required");
            if (!checkOut.isAfter(checkIn)) throw new IllegalArgumentException("Check-out must be after check-in");
            if (numberOfGuests <= 0) throw new IllegalArgumentException("Number of guests must be greater than zero");
            return new Reservation(this);
        }
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationNo='" + reservationNo + '\'' +
                ", guest=" + guest +
                ", room=" + room +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", numberOfGuests=" + numberOfGuests +
                ", specialRequest='" + specialRequest + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
