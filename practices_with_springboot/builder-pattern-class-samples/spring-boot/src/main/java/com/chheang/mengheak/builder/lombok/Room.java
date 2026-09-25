package com.chheang.mengheak.builder.lombok;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class Room {
	private  String roomCode;
    private  String roomName;
    private  BigDecimal price;
    
    

}
