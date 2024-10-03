package com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.responses;

import com.faroc.gymanager.sessionmanagement.api.sessionreservations.contracts.v1.responses.SessionReservationResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SessionResponse(
        UUID id,
        String name,
        String description,
        String category,
        int maxParticipants,
        Instant startTime,
        Instant endTime,
        UUID trainerId,
        List<SessionReservationResponse> sessionReservations
) {
}

