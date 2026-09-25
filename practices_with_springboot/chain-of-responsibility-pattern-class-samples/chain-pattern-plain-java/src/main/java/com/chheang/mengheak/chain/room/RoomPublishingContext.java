package com.chheang.mengheak.chain.room;

import java.util.UUID;

public record RoomPublishingContext(
        UUID roomId,
        UUID ownerId,
        Room room,
        Property property,
        boolean subscriptionActive,
        boolean moderationEnabled
) {
}
