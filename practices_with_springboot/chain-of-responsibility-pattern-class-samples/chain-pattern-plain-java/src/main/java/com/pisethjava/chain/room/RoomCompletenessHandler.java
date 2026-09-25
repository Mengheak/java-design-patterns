package com.pisethjava.chain.room;

public final class RoomCompletenessHandler extends BaseRoomPublishingHandler {

    @Override
    public void handle(RoomPublishingContext context) {
        Room room = context.room();

        if (room.name() == null || room.name().isBlank()
                || room.description() == null || room.description().isBlank()
                || room.price() == null) {
            throw new IllegalStateException("Room information is incomplete");
        }

        System.out.println("Room information complete");
        next(context);
    }
}
