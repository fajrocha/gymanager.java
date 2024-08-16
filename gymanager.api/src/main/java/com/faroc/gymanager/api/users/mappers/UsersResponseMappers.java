package com.faroc.gymanager.api.users.mappers;

import com.faroc.gymanager.application.users.DTOs.AuthDTO;
import com.faroc.gymanager.users.responses.AuthResponse;
import com.faroc.gymanager.users.responses.AdminCreatedResponse;

import java.util.UUID;

public class UsersResponseMappers {

    public static AuthResponse toResponse(AuthDTO authDTO) {
        return new AuthResponse(
                authDTO.id(),
                authDTO.firstName(),
                authDTO.lastName(),
                authDTO.email(),
                authDTO.token()
        );
    }
}
