package com.faroc.gymanager.sessionmanagement.api.sessionreservations.contracts.v1.requests;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MakeSessionReservationRequest(
        @NotNull(message = "Participant id must not be empty")
        UUID participantId) {
}
