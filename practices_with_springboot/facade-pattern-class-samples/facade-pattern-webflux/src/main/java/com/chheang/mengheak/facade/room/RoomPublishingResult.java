package com.chheang.mengheak.facade.room;

public record RoomPublishingResult(
        String roomId,
        String status,
        String moderationCaseId
) {
}
