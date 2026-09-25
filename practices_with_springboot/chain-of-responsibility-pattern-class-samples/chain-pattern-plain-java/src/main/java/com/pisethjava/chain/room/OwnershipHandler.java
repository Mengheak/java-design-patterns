package com.pisethjava.chain.room;

public final class OwnershipHandler extends BaseRoomPublishingHandler {

    @Override
    public void handle(RoomPublishingContext context) {
        if (!context.ownerId().equals(context.room().ownerId())) {
            throw new IllegalStateException("User does not own this room");
        }

        System.out.println("Ownership passed");
        next(context);
    }
}
