package com.pisethjava.chain.room;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record Room(
        UUID id,
        UUID ownerId,
        String name,
        String description,
        BigDecimal price,
        List<UUID> mediaIds
) {
}
