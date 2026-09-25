package com.pisethjava.builder.lesson07_reusable;

import java.math.BigDecimal;

public class ReusableBuilderDemo {
    public static void main(String[] args) {
        Room.Builder commonRoomBuilder = Room.builder()
                .price(BigDecimal.valueOf(50))
                .wifi(true);

        Room deluxe = commonRoomBuilder
                .roomCode("DLX001")
                .roomName("Deluxe Room")
                .build();

        Room suite = commonRoomBuilder
                .roomCode("STE001")
                .roomName("Suite Room")
                .price(BigDecimal.valueOf(120))
                .build();

        System.out.println(deluxe);
        System.out.println(suite);
        System.out.println("Reusable builder can share common configuration, but be careful with changed state.");
    }
}
