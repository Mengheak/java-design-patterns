package com.pisethjava.facade.room;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReactiveRoomPublishingFacade {

    private final RoomService roomService;
    private final PropertyClient propertyClient;
    private final SubscriptionClient subscriptionClient;
    private final MediaClient mediaClient;
    private final ModerationClient moderationClient;

    public ReactiveRoomPublishingFacade(
            RoomService roomService,
            PropertyClient propertyClient,
            SubscriptionClient subscriptionClient,
            MediaClient mediaClient,
            ModerationClient moderationClient
    ) {
        this.roomService = roomService;
        this.propertyClient = propertyClient;
        this.subscriptionClient = subscriptionClient;
        this.mediaClient = mediaClient;
        this.moderationClient = moderationClient;
    }

    public Mono<RoomPublishingResult> publish(String roomId, String ownerId) {
        return roomService.getRequired(roomId)
                .flatMap(room -> verifyRequirements(room, ownerId)
                        .then(roomService.submit(room.id())))
                .flatMap(submittedRoom -> moderationClient
                        .createCase(submittedRoom.id())
                        .map(caseId -> new RoomPublishingResult(
                                submittedRoom.id(),
                                submittedRoom.status(),
                                caseId
                        )));
    }

    private Mono<Void> verifyRequirements(Room room, String ownerId) {
        return Mono.when(
                propertyClient.verifyPublishable(room.propertyId(), ownerId),
                subscriptionClient.verifyActive(ownerId),
                mediaClient.verifyFiles(room.mediaIds())
        );
    }
}
