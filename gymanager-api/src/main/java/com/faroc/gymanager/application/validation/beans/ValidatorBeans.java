package com.faroc.gymanager.application.validation.beans;

import br.com.fluentvalidator.AbstractValidator;
import com.faroc.gymanager.application.users.commands.loginuser.LoginCommand;
import com.faroc.gymanager.application.users.commands.registeruser.RegisterUserCommand;
import com.faroc.gymanager.application.validation.ValidatorsAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorBeans {
    @Bean
    public ValidatorsAggregator<?, ?> validatorsAggregator() {
        return new ValidatorsAggregator<>();
    }

    @Bean
    public ValidatorsAggregator<?, RegisterUserCommand> addRegisterCommandValidator(
            ValidatorsAggregator<?, RegisterUserCommand> validatorsAggregator ,
            AbstractValidator<RegisterUserCommand> validator) {
        validatorsAggregator.getValidatorHashMap().put(RegisterUserCommand.class, validator);

        return validatorsAggregator;
    }

    @Bean
    public ValidatorsAggregator<?, LoginCommand> addLoginCommandValidator(
            ValidatorsAggregator<?, LoginCommand> validatorsAggregator,
            AbstractValidator<LoginCommand> validator) {
        validatorsAggregator.getValidatorHashMap().put(LoginCommand.class, validator);

        return validatorsAggregator;
    }
}
