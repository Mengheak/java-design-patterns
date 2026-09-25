package com.chheang.mengheak.chain.room.validation;

import com.chheang.mengheak.chain.room.domain.RoomPublishingContext;
import org.springframework.stereotype.Component;

@Component
public class MediaValidator implements RoomPublishingValidator {

    @Override
    public void validate(RoomPublishingContext context) {
        if (!context.hasMedia()) {
            throw new IllegalStateException("Room requires media");
        }
    }
}
