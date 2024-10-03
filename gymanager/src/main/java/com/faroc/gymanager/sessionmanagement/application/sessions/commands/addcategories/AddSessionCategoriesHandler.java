package com.faroc.gymanager.sessionmanagement.application.sessions.commands.addcategories;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionCategoriesGateway;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class AddSessionCategoriesHandler implements Command.Handler<AddSessionCategoriesCommand, List<SessionCategory>> {

    private final SessionCategoriesGateway sessionCategoriesGateway;

    @Autowired
    public AddSessionCategoriesHandler(SessionCategoriesGateway sessionCategoriesGateway) {
        this.sessionCategoriesGateway = sessionCategoriesGateway;
    }

    @Override
    @Transactional
    public List<SessionCategory> handle(AddSessionCategoriesCommand addSessionCategoriesCommand) {
        var sessionCategoriesToAdd = addSessionCategoriesCommand.sessionCategories();

        sessionCategoriesGateway.exceptExisting(sessionCategoriesToAdd);

        var sessionCategories = sessionCategoriesToAdd
                .stream()
                .map(category -> new SessionCategory(category))
                .toList();

        sessionCategoriesGateway.createAll(sessionCategories);

        return sessionCategories;
    }
}
