package com.chheang.mengheak.chain.room.domain;

public record RoomPublishingContext(
        String roomId,
        String ownerId,
        String roomOwnerId,
        boolean roomExists,
        boolean propertyApproved,
        boolean subscriptionActive,
        boolean complete,
        boolean hasMedia
) {
}
