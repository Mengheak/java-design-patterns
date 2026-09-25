package com.pisethjava.builder.lesson04_classic;

import java.math.BigDecimal;

public class ClassicBuilderDemo {
    public static void main(String[] args) {
        Room room = Room.builder()
                .roomCode("DLX001")
                .roomName("Deluxe Room")
                .price(BigDecimal.valueOf(80))
                .wifi(true)
                .balcony(true)
                .build();
        

        System.out.println(room);
        System.out.println("Builder makes object creation readable.");
    }
}