package com.faroc.gymanager.sessionmanagement.reservations.responses;

import java.util.UUID;

public record MakeSessionReservationResponse(UUID id, UUID participantId) {
}
