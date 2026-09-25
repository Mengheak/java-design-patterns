package com.pisethjava.chain.room;

public final class SubmitRoomHandler extends BaseRoomPublishingHandler {

    @Override
    public void handle(RoomPublishingContext context) {
        System.out.println("Room submitted: " + context.roomId());
    }
}
