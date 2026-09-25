package com.pisethjava.chain.room.api;

import com.pisethjava.chain.room.service.RoomPublishingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomPublishingController {

    private final RoomPublishingService service;

    public RoomPublishingController(RoomPublishingService service) {
        this.service = service;
    }

    @PostMapping("/{roomId}/submit")
    public SubmitRoomResponse submit(
            @PathVariable String roomId,
            @Valid @RequestBody SubmitRoomRequest request
    ) {
        String status = service.submit(roomId, request.ownerId());
        return new SubmitRoomResponse(roomId, status);
    }
}
