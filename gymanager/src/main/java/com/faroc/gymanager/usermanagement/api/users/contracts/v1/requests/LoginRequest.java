package com.faroc.gymanager.usermanagement.api.users.contracts.v1.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email must not be empty or omitted.")
        @Email(message = "Email provided must be a valid.")
        String email,

        @NotBlank(message = "Password must not be empty or omitted.")
        String password
) {
}
