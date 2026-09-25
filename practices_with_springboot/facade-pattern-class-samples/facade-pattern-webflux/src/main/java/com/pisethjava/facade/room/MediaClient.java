package com.pisethjava.facade.room;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class MediaClient {
    public Mono<Void> verifyFiles(List<String> mediaIds) {
        return Mono.empty();
    }
}
