package com.faroc.gymanager.usermanagement.application.users.dtos;

import java.util.UUID;

public record AuthDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String token){}
