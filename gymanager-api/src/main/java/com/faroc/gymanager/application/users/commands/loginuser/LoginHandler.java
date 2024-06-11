package com.faroc.gymanager.application.users.commands.loginuser;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.users.DTOs.AuthDTO;
import com.faroc.gymanager.application.users.exceptions.UnauthorizedException;
import com.faroc.gymanager.application.users.exceptions.UserNotFound;
import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.domain.users.errors.UserErrors;
import org.springframework.stereotype.Component;

@Component
public class LoginHandler implements Command.Handler<LoginCommand, AuthDTO> {
    private final UsersGateway usersGateway;
    private final PasswordHasher passwordHasher;

    public LoginHandler(UsersGateway usersGateway, PasswordHasher passwordHasher
    ) {
        this.usersGateway = usersGateway;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public AuthDTO handle(LoginCommand loginCommand) {
        var userEmail = loginCommand.email();
        var user = usersGateway
                .findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(UserErrors.notFound(userEmail), UserErrors.AUTH_FAILED));

        if (!user.validatePassword(loginCommand.password(), passwordHasher))
            throw new UnauthorizedException(UserErrors.authFailed(user.getId()), UserErrors.AUTH_FAILED);

        return new AuthDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), "");
    }
}
