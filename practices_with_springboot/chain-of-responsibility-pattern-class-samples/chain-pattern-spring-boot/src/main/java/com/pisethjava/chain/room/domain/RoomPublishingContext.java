package com.pisethjava.chain.room.domain;

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
