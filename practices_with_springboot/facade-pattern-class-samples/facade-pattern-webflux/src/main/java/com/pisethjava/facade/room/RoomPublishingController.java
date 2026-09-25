package com.pisethjava.facade.room;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.*;

@RestController @RequestMapping("/api/v1/rooms") public class RoomPublishingController {
    private final ReactiveRoomPublishingFacade facade;
    public RoomPublishingController(ReactiveRoomPublishingFacade f) {
        facade=f;
    }
    @PostMapping("/{roomId}/submit") public Mono<RoomPublishingResult> submit(@PathVariable String roomId,@RequestHeader("X-Owner-Id") String ownerId) {
        return facade.publish(roomId,ownerId);
    }
}
