package com.faroc.gymanager.users.requests;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password) {
}
