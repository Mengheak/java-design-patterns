package com.chheang.mengheak.chain.room;

public interface RoomPublishingHandler {
    void handle(RoomPublishingContext context);
    void setNext(RoomPublishingHandler next);
}
