package com.pisethjava.builder.lesson07_reusable;

import java.math.BigDecimal;

public final class Room {
    private final String roomCode;
    private final String roomName;
    private final BigDecimal price;
    private final boolean wifi;

    private Room(Builder builder) {
        this.roomCode = builder.roomCode;
        this.roomName = builder.roomName;
        this.price = builder.price;
        this.wifi = builder.wifi;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String roomCode;
        private String roomName;
        private BigDecimal price;
        private boolean wifi;

        public Builder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        public Builder roomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder wifi(boolean wifi) {
            this.wifi = wifi;
            return this;
        }

        public Room build() {
            return new Room(this);
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomCode='" + roomCode + '\'' +
                ", roomName='" + roomName + '\'' +
                ", price=" + price +
                ", wifi=" + wifi +
                '}';
    }
}
