package com.faroc.gymanager.application.validation;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.AbstractValidator;
import com.faroc.gymanager.application.shared.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ValidationMiddleware implements Command.Middleware {
    private final ValidatorsAggregator<?, ?> validatorsAggregator;

    public ValidationMiddleware(ValidatorsAggregator<?, ?> validatorsAggregator) {
        this.validatorsAggregator = validatorsAggregator;
    }

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        @SuppressWarnings("unchecked")
        var validator = (AbstractValidator<C>)validatorsAggregator.getValidatorHashMap().get(command.getClass());
        var result = validator.validate(command);

        if (result.isValid())
            next.invoke();

        Map<String, List<String>> modelState = new HashMap<>();

        result.getErrors().forEach(e -> {
            var fieldName = e.getField();
            var errorMessage = e.getMessage();

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

        throw new ValidationException(
                "Validation for " + command.getClass() + " request was invalid.",
                modelState);
    }
}

