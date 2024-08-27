package com.faroc.gymanager.sessionmanagement.sessions.responses;

import java.time.Instant;
import java.util.UUID;

public record SessionResponse(
        UUID id,
        String name,
        String description,
        String category,
        int maxParticipants,
        Instant startTime,
        Instant endTime,
        UUID trainerId
) {
}
