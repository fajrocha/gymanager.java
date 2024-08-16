package com.faroc.gymanager.application.users.commands.registeruser;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.users.DTOs.AuthDTO;

public record RegisterUserCommand(
        String firstName,
        String lastName,
        String email,
        String password) implements Command<AuthDTO> {
}
