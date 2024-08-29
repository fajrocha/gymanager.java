package com.faroc.gymanager.usermanagement.users.requests;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password) {
}
