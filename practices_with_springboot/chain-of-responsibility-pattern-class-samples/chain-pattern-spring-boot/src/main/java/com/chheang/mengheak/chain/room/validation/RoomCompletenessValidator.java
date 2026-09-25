package com.chheang.mengheak.chain.room.validation;

import com.chheang.mengheak.chain.room.domain.RoomPublishingContext;
import org.springframework.stereotype.Component;

@Component
public class RoomCompletenessValidator implements RoomPublishingValidator {

    @Override
    public void validate(RoomPublishingContext context) {
        if (!context.complete()) {
            throw new IllegalStateException("Room information is incomplete");
        }
    }
}
