package com.pisethjava.builder;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RoomResponse {
    private final String roomCode;
    private final String roomName;
    private final String roomType;
    private final BigDecimal price;
    private final String currency;
    private final boolean wifi;
    private final boolean balcony;
}
