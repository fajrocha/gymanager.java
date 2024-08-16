package com.faroc.gymanager.sessions.requests;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AddSessionRequest(
        String name,
        String description,
        String category,
        int maxParticipants,
        Instant startTime,
        Instant endTime,
        UUID trainerId
) {
}
