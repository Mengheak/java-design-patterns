package com.pisethjava.facade.room;

public record RoomPublishingResult(
        String roomId,
        String status,
        String moderationCaseId
) {
}
