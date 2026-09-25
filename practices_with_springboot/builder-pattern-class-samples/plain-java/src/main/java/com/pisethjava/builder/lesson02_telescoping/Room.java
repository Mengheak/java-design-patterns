package com.pisethjava.builder.lesson02_telescoping;

import java.math.BigDecimal;

public class Room {
    private final String roomCode;
    private final BigDecimal price;
    private final String currency;
    private final boolean wifi;

    public Room(String roomCode) {
        this(roomCode, BigDecimal.ZERO);
    }

    public Room(String roomCode, BigDecimal price) {
        this(roomCode, price, "USD");
    }

    public Room(String roomCode, BigDecimal price, String currency) {
        this(roomCode, price, currency, false);
    }

    public Room(String roomCode, BigDecimal price, String currency, boolean wifi) {
        this.roomCode = roomCode;
        this.price = price;
        this.currency = currency;
        this.wifi = wifi;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomCode='" + roomCode + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", wifi=" + wifi +
                '}';
    }
}
