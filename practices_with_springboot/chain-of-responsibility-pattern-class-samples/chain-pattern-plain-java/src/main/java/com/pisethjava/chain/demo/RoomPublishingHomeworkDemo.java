package com.pisethjava.chain.demo;

import com.pisethjava.chain.room.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class RoomPublishingHomeworkDemo {

    private RoomPublishingHomeworkDemo() {
    }

    public static void run() {
        System.out.println("\n=== HOMEWORK ANSWER: ROOM PUBLISHING ===");

        UUID ownerId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();

        Room room = new Room(
                roomId,
                ownerId,
                "Deluxe Room",
                "Large room with balcony",
                new BigDecimal("55.00"),
                List.of(UUID.randomUUID())
        );

        Property property = new Property(UUID.randomUUID(), true);

        RoomPublishingContext context = new RoomPublishingContext(
                roomId,
                ownerId,
                room,
                property,
                true,
                true
        );

        RoomPublishingHandler roomExists = new RoomExistsHandler();
        RoomPublishingHandler ownership = new OwnershipHandler();
        RoomPublishingHandler propertyStatus = new PropertyStatusHandler();
        RoomPublishingHandler subscription = new SubscriptionHandler();
        RoomPublishingHandler completeness = new RoomCompletenessHandler();
        RoomPublishingHandler media = new MediaHandler();
        RoomPublishingHandler moderation = new ModerationHandler();
        RoomPublishingHandler submit = new SubmitRoomHandler();

        roomExists.setNext(ownership);
        ownership.setNext(propertyStatus);
        propertyStatus.setNext(subscription);
        subscription.setNext(completeness);
        completeness.setNext(media);
        media.setNext(moderation);
        moderation.setNext(submit);

        roomExists.handle(context);
    }
}
