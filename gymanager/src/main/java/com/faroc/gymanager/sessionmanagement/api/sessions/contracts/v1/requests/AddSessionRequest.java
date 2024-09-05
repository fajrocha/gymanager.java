package com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record AddSessionRequest(
        @NotBlank(message = "Name must not be empty or omitted.")
        String name,
        @NotBlank(message = "Description must not be empty or omitted.")
        String description,
        @NotBlank(message = "Category must not be empty or omitted.")

        String category,
        @NotNull(message = "Max participants must not be empty or omitted.")
        @Min(value = 1, message = "Max participants must be greater or equal to 1.")
        int maxParticipants,
        @NotNull(message = "Start time must not be empty or omitted.")
        Instant startTime,
        @NotNull(message = "End time must not be empty or omitted.")
        Instant endTime,
        @NotNull(message = "Trainer id must not be empty or omitted.")
        UUID trainerId
) {
}
