package com.pisethjava.chain.room.api;

import jakarta.validation.constraints.NotBlank;

public record SubmitRoomRequest(@NotBlank String ownerId) {
}
