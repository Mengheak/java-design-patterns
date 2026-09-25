package com.pisethjava.chain.room;

public abstract class BaseRoomPublishingHandler implements RoomPublishingHandler {

    private RoomPublishingHandler next;

    @Override
    public void setNext(RoomPublishingHandler next) {
        this.next = next;
    }

    protected void next(RoomPublishingContext context) {
        if (next != null) {
            next.handle(context);
        }
    }
}
