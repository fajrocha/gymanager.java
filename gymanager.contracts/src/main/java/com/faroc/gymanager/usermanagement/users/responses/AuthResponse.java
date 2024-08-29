package com.faroc.gymanager.usermanagement.users.responses;

import java.util.UUID;

public record AuthResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String token){}