package com.pisethjava.chain.room;

public final class ModerationHandler extends BaseRoomPublishingHandler {

    @Override
    public void handle(RoomPublishingContext context) {
        System.out.println(
                context.moderationEnabled()
                        ? "Create moderation case"
                        : "Moderation skipped"
        );
        next(context);
    }
}
