package com.pisethjava.chain.room.validation;

import com.pisethjava.chain.room.domain.RoomPublishingContext;
import org.springframework.stereotype.Component;

@Component
public class RoomExistsValidator implements RoomPublishingValidator {

    @Override
    public void validate(RoomPublishingContext context) {
        if (!context.roomExists()) {
            throw new IllegalStateException("Room does not exist");
        }
    }
}
