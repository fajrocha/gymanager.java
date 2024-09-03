package com.faroc.gymanager.usermanagement.api.users.requests.v1;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password) {
}
