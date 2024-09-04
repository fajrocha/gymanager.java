package com.faroc.gymanager.sessionmanagement.api.sessionreservations.contracts.v1.responses;

import java.util.UUID;

public record MakeSessionReservationResponse(UUID id, UUID participantId) {
}
