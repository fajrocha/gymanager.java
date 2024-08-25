package com.faroc.gymanager.usermanagement.application.users.commands.loginuser;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.usermanagement.application.users.dtos.AuthDTO;
import com.faroc.gymanager.common.application.security.exceptions.UnauthorizedException;
import com.faroc.gymanager.usermanagement.application.users.gateways.TokenGenerator;
import com.faroc.gymanager.usermanagement.application.users.gateways.UsersGateway;
import com.faroc.gymanager.usermanagement.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.usermanagement.domain.users.errors.UserErrors;
import org.springframework.stereotype.Component;

@Component
public class LoginHandler implements Command.Handler<LoginCommand, AuthDTO> {
    private final UsersGateway usersGateway;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    public LoginHandler(
            UsersGateway usersGateway,
            PasswordHasher passwordHasher,
            TokenGenerator tokenGenerator
    ) {
        this.usersGateway = usersGateway;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AuthDTO handle(LoginCommand loginCommand) {
        var userEmail = loginCommand.email();
        var user = usersGateway
                .findByEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedException(UserErrors.notFound(userEmail)));

        if (!user.validatePassword(loginCommand.password(), passwordHasher))
            throw new UnauthorizedException(UserErrors.authFailed(user.getId()));

        var token = tokenGenerator.generate(user);

        return new AuthDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), token);
    }
}
