package com.chheang.mengheak.chain.room.validation;

import com.chheang.mengheak.chain.room.domain.RoomPublishingContext;
import org.springframework.stereotype.Component;

@Component
public class OwnershipValidator implements RoomPublishingValidator {

    @Override
    public void validate(RoomPublishingContext context) {
        if (!context.ownerId().equals(context.roomOwnerId())) {
            throw new IllegalStateException("User does not own this room");
        }
    }
}
