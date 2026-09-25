package com.pisethjava.chain.room.service;

import com.pisethjava.chain.room.domain.RoomPublishingContext;
import com.pisethjava.chain.room.validation.RoomPublishingValidationChain;
import org.springframework.stereotype.Service;

@Service
public class RoomPublishingService {

    private final RoomPublishingValidationChain validationChain;

    public RoomPublishingService(RoomPublishingValidationChain validationChain) {
        this.validationChain = validationChain;
    }

    public String submit(String roomId, String ownerId) {
        RoomPublishingContext context = new RoomPublishingContext(
                roomId,
                ownerId,
                ownerId,
                true,
                true,
                true,
                true,
                true
        );

        validationChain.validate(context);
        return "SUBMITTED";
    }
}
