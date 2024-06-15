package com.faroc.gymanager.application.validation;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.context.ValidationResult;
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
    @SuppressWarnings("unchecked")
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        var validator = (AbstractValidator<C>)validatorsAggregator.getValidatorHashMap().get(command.getClass());

        if (validator == null)
            return next.invoke();

        var result = validator.validate(command);

        if (result.isValid())
            return next.invoke();

        Map<String, List<String>> modelState = getErrorsMap(result);

        throw new ValidationException(
                "Validation for " + command.getClass() + " request was invalid.",
                modelState);
    }

    private static Map<String, List<String>> getErrorsMap(ValidationResult result) {
        Map<String, List<String>> modelState = new HashMap<>();

        result.getErrors().forEach(error -> {
            var fieldName = error.getField();
            var errorMessage = error.getMessage();

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

        return modelState;
    }
}

