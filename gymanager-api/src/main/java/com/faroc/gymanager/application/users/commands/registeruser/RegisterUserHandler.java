package com.faroc.gymanager.application.users.commands.registeruser;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.users.DTOs.AuthDTO;
import com.faroc.gymanager.application.users.exceptions.EmailAlreadyExistsException;
import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.users.User;
import com.faroc.gymanager.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.domain.users.errors.UserErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserHandler implements Command.Handler<RegisterUserCommand, AuthDTO>{
    private final UsersGateway usersGateway;
    private final PasswordHasher passwordHasher;

    @Autowired
    public RegisterUserHandler(
            UsersGateway usersGateway, 
            PasswordHasher passwordHasher) {
        this.usersGateway = usersGateway;
        this.passwordHasher = passwordHasher;
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

        return new AuthDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), "");
    }
}
