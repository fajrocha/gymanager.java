package com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses;

import java.util.UUID;

public record AuthResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String token){}