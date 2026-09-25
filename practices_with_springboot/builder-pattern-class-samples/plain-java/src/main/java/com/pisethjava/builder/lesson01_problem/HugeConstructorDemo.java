package com.pisethjava.builder.lesson01_problem;

import java.math.BigDecimal;

public class HugeConstructorDemo {
    public static void main(String[] args) {
        Room room = new Room(
                "DLX001",
                "Deluxe Room",
                "DELUXE",
                2,
                1,
                BigDecimal.valueOf(80),
                "USD",
                true,
                false,
                true
        );

        System.out.println(room);
        System.out.println("Problem: hard to know what each parameter means without opening constructor.");
    }
}
