package com.faroc.gymanager.application.users.commands.loginuser;

import br.com.fluentvalidator.AbstractValidator;
import com.faroc.gymanager.application.users.commands.registeruser.RegisterUserCommand;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.LogicalPredicate.isTrue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static java.util.function.Predicate.not;

@Component
public class LoginUserValidator extends AbstractValidator<LoginCommand> {
    @Override
    public void rules() {
        // Email
        ruleFor(LoginCommand::email)
                .must(not(stringEmptyOrNull()))
                .withFieldName("email")
                .withMessage("Email must not be empty or omitted.");

        ruleFor(LoginCommand::email)
                .must(isTrue(email -> EmailValidator.getInstance().isValid(email)))
                .withFieldName("email")
                .withMessage("Email provided must be valid.");

        // Password
        ruleFor(LoginCommand::password)
                .must(not(stringEmptyOrNull()))
                .withFieldName("password")
                .withMessage("Password must not be empty or omitted.");
    }
}
