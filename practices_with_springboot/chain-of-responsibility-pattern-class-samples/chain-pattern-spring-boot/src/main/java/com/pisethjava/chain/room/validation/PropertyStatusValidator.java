package com.pisethjava.chain.room.validation;

import com.pisethjava.chain.room.domain.RoomPublishingContext;
import org.springframework.stereotype.Component;

@Component
public class PropertyStatusValidator implements RoomPublishingValidator {

    @Override
    public void validate(RoomPublishingContext context) {
        if (!context.propertyApproved()) {
            throw new IllegalStateException("Property must be approved");
        }
    }
}
