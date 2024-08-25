package com.faroc.gymanager.usermanagement.application.users.commands.registeruser;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.usermanagement.application.users.dtos.AuthDTO;

public record RegisterUserCommand(
        String firstName,
        String lastName,
        String email,
        String password) implements Command<AuthDTO> {
}
