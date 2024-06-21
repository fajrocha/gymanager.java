package com.faroc.gymanager.application.validation;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.AbstractValidator;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Getter
public class ValidatorsAggregator<R, C extends Command<R>> {
    private final HashMap<Class<C>, AbstractValidator<C>> validatorHashMap = new HashMap<>();
}
