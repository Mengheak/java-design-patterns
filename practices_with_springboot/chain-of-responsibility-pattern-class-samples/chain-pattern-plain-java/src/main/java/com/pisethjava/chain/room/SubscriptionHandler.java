package com.pisethjava.chain.room;

public final class SubscriptionHandler extends BaseRoomPublishingHandler {

    @Override
    public void handle(RoomPublishingContext context) {
        if (!context.subscriptionActive()) {
            throw new IllegalStateException("Active subscription is required");
        }

        System.out.println("Subscription active");
        next(context);
    }
}
