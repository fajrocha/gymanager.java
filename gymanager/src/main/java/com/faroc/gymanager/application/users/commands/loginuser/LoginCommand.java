package com.faroc.gymanager.application.users.commands.loginuser;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.users.DTOs.AuthDTO;

public record LoginCommand(String email, String password) implements Command<AuthDTO> {
}
