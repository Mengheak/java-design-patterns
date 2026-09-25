package com.pisethjava.chain.room;

public final class RoomExistsHandler extends BaseRoomPublishingHandler {

    @Override
    public void handle(RoomPublishingContext context) {
        if (context.room() == null) {
            throw new IllegalStateException("Room does not exist");
        }

        System.out.println("Room exists");
        next(context);
    }
}
