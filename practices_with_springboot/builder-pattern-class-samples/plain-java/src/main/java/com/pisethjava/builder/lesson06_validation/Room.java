package com.pisethjava.builder.lesson06_validation;

import java.math.BigDecimal;

public final class Room {
    private final String roomCode;
    private final BigDecimal price;
    private final String currency;

    private Room(Builder builder) {
        this.roomCode = builder.roomCode;
        this.price = builder.price;
        this.currency = builder.currency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String roomCode;
        private BigDecimal price;
        private String currency;

        public Builder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Room build() {
            if (roomCode == null || roomCode.isBlank()) {
                throw new IllegalArgumentException("Room code is required");
            }
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be greater than zero");
            }
            if (currency == null || currency.isBlank()) {
                throw new IllegalArgumentException("Currency is required");
            }
            return new Room(this);
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomCode='" + roomCode + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
