package com.faroc.gymanager.application.validation;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.AbstractValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ValidatorsAggregator<R, C extends Command<R>> {
    private  HashMap<Class<C>, AbstractValidator<C>> validatorHashMap = new HashMap<>();

    public HashMap<Class<C>, AbstractValidator<C>> getValidatorHashMap() {
        return validatorHashMap;
    }
}
