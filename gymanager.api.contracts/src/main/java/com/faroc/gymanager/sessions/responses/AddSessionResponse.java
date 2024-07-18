package com.faroc.gymanager.sessions.responses;

import java.time.Instant;
import java.util.UUID;

public record AddSessionResponse(
        String name,
        String description,
        String category,
        int maxParticipants,
        Instant startTime,
        Instant endTime,
        UUID trainerId
) {
}
