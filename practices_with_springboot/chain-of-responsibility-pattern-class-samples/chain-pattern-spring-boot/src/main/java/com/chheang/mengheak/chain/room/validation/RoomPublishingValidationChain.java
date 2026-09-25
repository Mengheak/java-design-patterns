package com.chheang.mengheak.chain.room.validation;

import com.chheang.mengheak.chain.room.domain.RoomPublishingContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomPublishingValidationChain {

    private final List<RoomPublishingValidator> validators;

    public RoomPublishingValidationChain(
            RoomExistsValidator roomExistsValidator,
            OwnershipValidator ownershipValidator,
            PropertyStatusValidator propertyStatusValidator,
            SubscriptionValidator subscriptionValidator,
            RoomCompletenessValidator roomCompletenessValidator,
            MediaValidator mediaValidator
    ) {
        this.validators = List.of(
                roomExistsValidator,
                ownershipValidator,
                propertyStatusValidator,
                subscriptionValidator,
                roomCompletenessValidator,
                mediaValidator
        );
    }

    public void validate(RoomPublishingContext context) {
        for (RoomPublishingValidator validator : validators) {
            validator.validate(context);
        }
    }
}
