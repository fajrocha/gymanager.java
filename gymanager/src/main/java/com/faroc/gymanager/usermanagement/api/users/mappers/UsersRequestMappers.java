package com.faroc.gymanager.usermanagement.api.users.mappers;

import com.faroc.gymanager.usermanagement.application.users.commands.loginuser.LoginCommand;
import com.faroc.gymanager.usermanagement.application.users.commands.registeruser.RegisterUserCommand;
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.requests.LoginRequest;
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.requests.RegisterRequest;

public class UsersRequestMappers {
    public static RegisterUserCommand toCommand(RegisterRequest request) {
        return new RegisterUserCommand(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password()
        );
    }

    public static LoginCommand toCommand(LoginRequest request) {
        return new LoginCommand(
                request.email(),
                request.password()
        );
    }
}
