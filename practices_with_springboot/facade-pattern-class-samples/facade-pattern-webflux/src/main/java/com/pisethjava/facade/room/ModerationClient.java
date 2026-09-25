package com.pisethjava.facade.room;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ModerationClient {
    public Mono<String> createCase(String roomId) {
        return Mono.just("CASE-" + roomId);
    }
}
