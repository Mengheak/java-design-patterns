package com.pisethjava.facade.room;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RoomService {
    public Mono<Room> getRequired(String roomId) {
        return Mono.just(new Room(roomId, "PROPERTY-1", List.of("MEDIA-1"), "DRAFT"));
    }

    public Mono<Room> submit(String roomId) {
        return Mono.just(new Room(roomId, "PROPERTY-1", List.of("MEDIA-1"), "SUBMITTED"));
    }
}
