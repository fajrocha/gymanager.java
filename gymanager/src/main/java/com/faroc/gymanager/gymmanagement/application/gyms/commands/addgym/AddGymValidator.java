package com.faroc.gymanager.gymmanagement.application.gyms.commands.addgym;

import br.com.fluentvalidator.AbstractValidator;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static java.util.function.Predicate.not;

@Component
public class AddGymValidator extends AbstractValidator<AddGymCommand> {
    @Override
    public void rules() {
        // Name
        ruleFor(AddGymCommand::name)
                .must(not(stringEmptyOrNull()))
                .withFieldName("name")
                .withMessage("Name must not be empty or omitted.");
    }
}

