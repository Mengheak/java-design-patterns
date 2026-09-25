package com.pisethjava.builder.lesson02_telescoping;

import java.math.BigDecimal;

public class TelescopingConstructorDemo {
    public static void main(String[] args) {
        Room simpleRoom = new Room("STD001");
        Room deluxeRoom = new Room("DLX001", BigDecimal.valueOf(80), "USD", true);

        System.out.println(simpleRoom);
        System.out.println(deluxeRoom);
        System.out.println("Problem: more optional fields create more constructors.");
    }
}
