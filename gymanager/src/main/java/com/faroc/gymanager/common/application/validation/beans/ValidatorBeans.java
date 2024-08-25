package com.faroc.gymanager.common.application.validation.beans;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.AbstractValidator;
import com.faroc.gymanager.common.application.validation.ValidatorsAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;

import java.util.List;

@Configuration
public class ValidatorBeans {
    @Bean
    public <R, C extends Command<R>> ValidatorsAggregator<R, C> validatorsAggregator() {
        return new ValidatorsAggregator<>();
    }

    @Bean
    @SuppressWarnings("unchecked")
    public <R, C extends Command<R>> ValidatorsAggregator<R, C> addCommandValidators(
            ValidatorsAggregator<R, C> validatorsAggregator,
            List<AbstractValidator<C>> validators) {

        for (AbstractValidator<C> validator : validators) {
            ResolvableType resolvableType = ResolvableType.forClass(validator.getClass());
            ResolvableType genericType = resolvableType.as(AbstractValidator.class).getGeneric(0);

            Class<C> commandType = (Class<C>)genericType.resolve();

            validatorsAggregator.getValidatorHashMap().put(commandType, validator);
        }

        return validatorsAggregator;
    }
}
