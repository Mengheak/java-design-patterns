package com.pisethjava.builder.lesson01_problem;

import java.math.BigDecimal;

public class Room {
    private final String roomCode;
    private final String roomName;
    private final String roomType;
    private final int maxAdult;
    private final int maxChild;
    private final BigDecimal price;
    private final String currency;
    private final boolean wifi;
    private final boolean kitchen;
    private final boolean balcony;

    public Room(String roomCode, String roomName, String roomType, int maxAdult, int maxChild,
                BigDecimal price, String currency, boolean wifi, boolean kitchen, boolean balcony) {
        this.roomCode = roomCode;
        this.roomName = roomName;
        this.roomType = roomType;
        this.maxAdult = maxAdult;
        this.maxChild = maxChild;
        this.price = price;
        this.currency = currency;
        this.wifi = wifi;
        this.kitchen = kitchen;
        this.balcony = balcony;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomCode='" + roomCode + '\'' +
                ", roomName='" + roomName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", maxAdult=" + maxAdult +
                ", maxChild=" + maxChild +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", wifi=" + wifi +
                ", kitchen=" + kitchen +
                ", balcony=" + balcony +
                '}';
    }
}
