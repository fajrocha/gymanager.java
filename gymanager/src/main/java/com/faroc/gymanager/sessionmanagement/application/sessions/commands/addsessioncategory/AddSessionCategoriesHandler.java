package com.faroc.gymanager.sessionmanagement.application.sessions.commands.addsessioncategory;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.sessionmanagement.domain.sessions.errors.SessionCategoriesErrors;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddSessionCategoriesHandler implements Command.Handler<AddSessionCategoriesCommand, List<String>> {
    private final GymsGateway gymsGateway;

    @Autowired
    public AddSessionCategoriesHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @Override
    public List<String> handle(AddSessionCategoriesCommand command) {
        var gymId = command.gymId();

        var gym = gymsGateway.findById(gymId).orElseThrow(() -> new UnexpectedException(
                SessionCategoriesErrors.gymNotFound(gymId),
                SessionCategoriesErrors.GYM_NOT_FOUND
        ));

        var sessionCategories = command.sessionCategories();
        sessionCategories.forEach(gym::addCategory);

        gymsGateway.update(gym);

        return sessionCategories;
    }
}
