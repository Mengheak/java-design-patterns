package com.chheang.mengheak.facade.room;

import java.util.List;

public record Room(
        String id,
        String propertyId,
        List<String> mediaIds,
        String status
) {
}
