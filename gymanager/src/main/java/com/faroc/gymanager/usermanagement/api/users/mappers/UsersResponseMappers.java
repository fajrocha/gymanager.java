package com.faroc.gymanager.usermanagement.api.users.mappers;

import com.faroc.gymanager.usermanagement.application.users.dtos.AuthDTO;
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.AuthResponse;

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
