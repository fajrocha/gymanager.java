package com.faroc.gymanager.gymmanagement.api.gyms.contracts.v1.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddGymRequest(
        @NotBlank(message = "Name must not be empty or omitted.")
        @Size(min = 1, max = 50, message = "Name must have between 1 and 50 characters.")
        String name) {
}
