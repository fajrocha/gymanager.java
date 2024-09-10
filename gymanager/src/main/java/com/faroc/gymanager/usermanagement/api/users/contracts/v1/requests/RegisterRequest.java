package com.faroc.gymanager.usermanagement.api.users.contracts.v1.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "First name must not be empty or omitted.")
        @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
        String firstName,
        @NotBlank(message = "Last name must not be empty or omitted.")
        @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
        String lastName,
        @NotBlank(message = "Email must not be empty or omitted.")
        @Email(message = "Email provided must be a valid.")
        @Size(min = 1, max = 62, message = "Email must be between 1 and 62 characters")
        String email,
        @NotBlank(message = "Password must not be empty or omitted.")
        @Size(min = 1, max = 40, message = "Email must be between 1 and 40 characters")
        String password) {
}
