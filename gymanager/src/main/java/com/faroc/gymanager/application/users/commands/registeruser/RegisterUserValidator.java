package com.faroc.gymanager.application.users.commands.registeruser;

import br.com.fluentvalidator.AbstractValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.LogicalPredicate.isTrue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static java.util.function.Predicate.not;

@Component
public class RegisterUserValidator extends AbstractValidator<RegisterUserCommand> {
    @Override
    public void rules() {
        // firstName
        ruleFor(RegisterUserCommand::firstName)
                .must(not(stringEmptyOrNull()))
                .withFieldName("firstName")
                .withMessage("First name must not be empty or omitted.");

        // lastName
        ruleFor(RegisterUserCommand::lastName)
                .must(not(stringEmptyOrNull()))
                .withFieldName("lastName")
                .withMessage("Last name must not be empty or omitted.");

        // Email
        ruleFor(RegisterUserCommand::email)
                .must(not(stringEmptyOrNull()))
                .withFieldName("email")
                .withMessage("Email must not be empty or omitted.");

        ruleFor(RegisterUserCommand::email)
                .must(isTrue(email -> EmailValidator.getInstance().isValid(email)))
                .withFieldName("email")
                .withMessage("Email provided must be valid.");

        // Password
        ruleFor(RegisterUserCommand::password)
                .must(not(stringEmptyOrNull()))
                .withFieldName("password")
                .withMessage("Password must not be empty or omitted.");
    }
}
