package com.faroc.gymanager.api.users.mappers;

import com.faroc.gymanager.application.users.commands.loginuser.LoginCommand;
import com.faroc.gymanager.application.users.commands.registeruser.RegisterUserCommand;
import com.faroc.gymanager.users.requests.LoginRequest;
import com.faroc.gymanager.users.requests.RegisterRequest;

public class UsersRequestMappers {
    public static RegisterUserCommand toCommand(RegisterRequest registerRequest) {
        return new RegisterUserCommand(
                registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.email(),
                registerRequest.password()
        );
    }

    public static LoginCommand toCommand(LoginRequest loginRequest) {
        return new LoginCommand(
                loginRequest.email(),
                loginRequest.password()
        );
    }
}
