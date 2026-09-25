package com.pisethjava.builder.lombok;

public class BuilderLombokDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Room room = new Room();
		//room.
		
		Room room = Room.builder()
				.roomCode("A01")
				.roomName("Deluxe")
				.build();
		
		System.out.println(room);

	}

}
