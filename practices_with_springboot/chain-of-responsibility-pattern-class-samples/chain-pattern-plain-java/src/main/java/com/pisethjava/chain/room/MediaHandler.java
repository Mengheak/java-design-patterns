package com.pisethjava.chain.room;

public final class MediaHandler extends BaseRoomPublishingHandler {

    @Override
    public void handle(RoomPublishingContext context) {
        if (context.room().mediaIds() == null || context.room().mediaIds().isEmpty()) {
            throw new IllegalStateException("At least one room image is required");
        }

        System.out.println("Media validation passed");
        next(context);
    }
}
