package com.faroc.gymanager.reservations.responses;

import java.util.UUID;

public record MakeSessionReservationResponse(UUID id, UUID participantId) {
}
