package com.pisethjava.chain.room;

public final class PropertyStatusHandler extends BaseRoomPublishingHandler {

    @Override
    public void handle(RoomPublishingContext context) {
        if (context.property() == null || !context.property().approved()) {
            throw new IllegalStateException("Property must be approved");
        }

        System.out.println("Property approved");
        next(context);
    }
}
