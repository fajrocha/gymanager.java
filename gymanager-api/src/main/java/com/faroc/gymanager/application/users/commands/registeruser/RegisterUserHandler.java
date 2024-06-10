package com.faroc.gymanager.application.users.commands.registeruser;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.AbstractValidator;
import com.faroc.gymanager.application.users.DTOs.AuthDTO;
import com.faroc.gymanager.application.users.exceptions.EmailAlreadyExistsException;
import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.users.User;
import com.faroc.gymanager.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.domain.users.errors.UserErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegisterUserHandler implements Command.Handler<RegisterUserCommand, AuthDTO>{
    private final UsersGateway usersGateway;
    private final PasswordHasher passwordHasher;
    private final AbstractValidator<RegisterUserCommand> validator;

    @Autowired
    public RegisterUserHandler(
            UsersGateway usersGateway,
            PasswordHasher passwordHasher,
            AbstractValidator<RegisterUserCommand> validator
    ) {
        this.usersGateway = usersGateway;
        this.passwordHasher = passwordHasher;
        this.validator = validator;
    }

    @Override
    public AuthDTO handle(RegisterUserCommand registerUserCommand) {
        var validationResult = validator.validate(registerUserCommand);

        Map<String, List<String>> modelState = new HashMap<>();

        validationResult.getErrors().stream().forEach(errorToAdd -> {
            var fieldName = errorToAdd.getField();
            var errorMessage = errorToAdd.getMessage();

            List<String> errors;
            if (modelState.containsKey(fieldName)) {
                errors = modelState.get(fieldName);
                errors.add(errorMessage);
            }
            else {
                errors = new ArrayList<>();
                errors.add(errorMessage);
            }
            modelState.put(fieldName, errors);
        });

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
