package com.faroc.gymanager.usermanagement.api.users.responses.v1;

import java.util.UUID;

public record AuthResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String token){}