package com.pisethjava.builder.lesson03_javabeans;

import java.math.BigDecimal;

public class Room {
    private String roomCode;
    private String roomName;
    private BigDecimal price;
    private boolean wifi;

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
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
