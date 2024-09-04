package com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.responses;

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
