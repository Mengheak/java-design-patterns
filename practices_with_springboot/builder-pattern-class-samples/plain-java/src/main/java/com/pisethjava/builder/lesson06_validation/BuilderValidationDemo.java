package com.pisethjava.builder.lesson06_validation;

import java.math.BigDecimal;

public class BuilderValidationDemo {
    public static void main(String[] args) {
        Room validRoom = Room.builder()
                .roomCode("DLX001")
                .price(BigDecimal.valueOf(80))
                .currency("USD")
                .build();

        System.out.println(validRoom);

        try {
            Room.builder().build();
        } catch (IllegalArgumentException exception) {
            System.out.println("Validation works: " + exception.getMessage());
        }
        
    }
    
}
