package com.faroc.gymanager.application.users.commands.registeruser;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.AbstractValidator;
import com.faroc.gymanager.application.users.DTOs.AuthDTO;
import com.faroc.gymanager.application.users.exceptions.EmailAlreadyExistsException;
import com.faroc.gymanager.application.users.gateways.TokenGenerator;
import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.users.User;
import com.faroc.gymanager.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.domain.users.errors.UserErrors;
import com.faroc.gymanager.infrastructure.users.authentication.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegisterUserHandler implements Command.Handler<RegisterUserCommand, AuthDTO>{
    private final UsersGateway usersGateway;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public RegisterUserHandler(
            UsersGateway usersGateway,
            PasswordHasher passwordHasher,
            TokenGenerator tokenGenerator
    ) {
        this.usersGateway = usersGateway;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AuthDTO handle(RegisterUserCommand registerUserCommand) {
        if (usersGateway.emailExists(registerUserCommand.email()))
            throw new EmailAlreadyExistsException(UserErrors.EMAIL_ALREADY_EXISTS);

        var pwdHash = passwordHasher.HashPassword(registerUserCommand.password());

        var user = new User(
                registerUserCommand.firstName(),
                registerUserCommand.lastName(),
                registerUserCommand.email(),
                pwdHash);
        usersGateway.save(user);

        var token = tokenGenerator.generate(user);

        return new AuthDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), "");
    }
}
