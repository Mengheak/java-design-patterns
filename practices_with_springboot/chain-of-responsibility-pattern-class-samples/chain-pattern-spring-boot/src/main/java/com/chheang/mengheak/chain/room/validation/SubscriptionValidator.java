package com.chheang.mengheak.chain.room.validation;

import com.chheang.mengheak.chain.room.domain.RoomPublishingContext;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionValidator implements RoomPublishingValidator {

    @Override
    public void validate(RoomPublishingContext context) {
        if (!context.subscriptionActive()) {
            throw new IllegalStateException("Subscription is inactive");
        }
    }
}
