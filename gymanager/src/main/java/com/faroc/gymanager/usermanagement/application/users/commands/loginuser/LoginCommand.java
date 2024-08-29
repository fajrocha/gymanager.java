package com.faroc.gymanager.usermanagement.application.users.commands.loginuser;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.usermanagement.application.users.dtos.AuthDTO;

public record LoginCommand(String email, String password) implements Command<AuthDTO> {
}
