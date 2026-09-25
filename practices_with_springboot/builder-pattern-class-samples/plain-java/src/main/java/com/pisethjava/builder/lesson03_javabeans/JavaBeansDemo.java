package com.pisethjava.builder.lesson03_javabeans;

import java.math.BigDecimal;

public class JavaBeansDemo {
    public static void main(String[] args) {
        Room room = new Room();
        room.setRoomCode("DLX001");
        room.setRoomName("Deluxe Room");
        room.setPrice(BigDecimal.valueOf(80));
        room.setWifi(true);

        System.out.println(room);

        room.setPrice(null);
        System.out.println("After accidental change: " + room);
        System.out.println("Problem: object can be incomplete or changed later.");
    }
}
