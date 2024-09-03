package com.faroc.gymanager.usermanagement.api.users.requests.v1;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email must not be empty or omitted.")
        @Email(message = "Email provided must be a vali.")
        String email,

        @NotBlank(message = "Password must not be empty or omitted.")
        String password
) {
}
