package com.pisethjava.builder.lesson09_homework;

import java.math.BigDecimal;

public final class Room {
    private final String roomCode;
    private final String roomType;
    private final BigDecimal price;
    private final String currency;
    private final int maxAdult;
    private final int maxChild;
    private final boolean wifi;
    private final boolean balcony;

    private Room(Builder builder) {
        this.roomCode = builder.roomCode;
        this.roomType = builder.roomType;
        this.price = builder.price;
        this.currency = builder.currency;
        this.maxAdult = builder.maxAdult;
        this.maxChild = builder.maxChild;
        this.wifi = builder.wifi;
        this.balcony = builder.balcony;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String roomCode;
        private String roomType;
        private BigDecimal price;
        private String currency;
        private int maxAdult;
        private int maxChild;
        private boolean wifi;
        private boolean balcony;

        public Builder roomCode(String roomCode) { this.roomCode = roomCode; return this; }
        public Builder roomType(String roomType) { this.roomType = roomType; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }
        public Builder maxAdult(int maxAdult) { this.maxAdult = maxAdult; return this; }
        public Builder maxChild(int maxChild) { this.maxChild = maxChild; return this; }
        public Builder wifi(boolean wifi) { this.wifi = wifi; return this; }
        public Builder balcony(boolean balcony) { this.balcony = balcony; return this; }

        public Room build() {
            if (roomCode == null || roomCode.isBlank()) throw new IllegalArgumentException("Room code is required");
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Price must be greater than zero");
            if (currency == null || currency.isBlank()) throw new IllegalArgumentException("Currency is required");
            return new Room(this);
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomCode='" + roomCode + '\'' +
                ", roomType='" + roomType + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", maxAdult=" + maxAdult +
                ", maxChild=" + maxChild +
                ", wifi=" + wifi +
                ", balcony=" + balcony +
                '}';
    }
}
