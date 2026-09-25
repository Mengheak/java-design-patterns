package com.pisethjava.facade.room;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SubscriptionClient {
    public Mono<Void> verifyActive(String ownerId) {
        return Mono.empty();
    }
}
