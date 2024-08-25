package com.faroc.gymanager.usermanagement.application.users.commands.registeruser;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.usermanagement.application.users.dtos.AuthDTO;
import com.faroc.gymanager.usermanagement.application.users.exceptions.EmailAlreadyExistsException;
import com.faroc.gymanager.usermanagement.application.users.gateways.TokenGenerator;
import com.faroc.gymanager.usermanagement.application.users.gateways.UsersGateway;
import com.faroc.gymanager.usermanagement.domain.users.User;
import com.faroc.gymanager.usermanagement.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.usermanagement.domain.users.errors.UserErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        var userEmail = registerUserCommand.email();
        if (usersGateway.emailExists(userEmail))
            throw new EmailAlreadyExistsException(UserErrors.EMAIL_ALREADY_EXISTS);

        var pwdHash = passwordHasher.hashPassword(registerUserCommand.password());

        var user = new User(
                registerUserCommand.firstName(),
                registerUserCommand.lastName(),
                userEmail,
                pwdHash);
        usersGateway.create(user);

        var token = tokenGenerator.generate(user);

        return new AuthDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), token);
    }
}
