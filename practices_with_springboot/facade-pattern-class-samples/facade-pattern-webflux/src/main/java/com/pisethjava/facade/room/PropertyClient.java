package com.pisethjava.facade.room;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PropertyClient {
    public Mono<Void> verifyPublishable(String propertyId, String ownerId) {
        return Mono.empty();
    }
}
