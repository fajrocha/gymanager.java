package com.faroc.gymanager.application.users.DTOs;

import java.util.UUID;

public record AuthDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String token){};
