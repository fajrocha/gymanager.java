package com.faroc.gymanager.usermanagement.application.users.commands.loginuser;

import br.com.fluentvalidator.AbstractValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.LogicalPredicate.isTrue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static java.util.function.Predicate.not;

@Component
public class LoginUserValidator extends AbstractValidator<LoginCommand> {
    public static final String EMAIL_FIELD_NAME = "email";
    public static final String EMAIL_EMPTY_MESSAGE = "Email must not be empty or omitted.";
    public static final String EMAIL_INVALID_MESSAGE = "Email provided must be valid.";

    public static final String PASSWORD_FIELD_NAME = "password";
    public static final String PASSWORD_EMPTY_MESSAGE = "Password must not be empty or omitted.";

    @Override
    public void rules() {
        // Email
        ruleFor(LoginCommand::email)
                .must(not(stringEmptyOrNull()))
                .withFieldName(EMAIL_FIELD_NAME)
                .withMessage(EMAIL_EMPTY_MESSAGE);

        ruleFor(LoginCommand::email)
                .must(isTrue(email -> EmailValidator.getInstance().isValid(email)))
                .withFieldName(EMAIL_FIELD_NAME)
                .withMessage(EMAIL_INVALID_MESSAGE);

        // Password
        ruleFor(LoginCommand::password)
                .must(not(stringEmptyOrNull()))
                .withFieldName(PASSWORD_FIELD_NAME)
                .withMessage(PASSWORD_EMPTY_MESSAGE);
    }
}
