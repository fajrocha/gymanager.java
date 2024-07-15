package com.faroc.gymanager.application.users.commands.addtrainer;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.security.CurrentUserProvider;
import com.faroc.gymanager.application.security.exceptions.UnauthorizedException;
import com.faroc.gymanager.application.shared.abstractions.DomainEventsHandler;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.users.errors.UserErrors;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AddTrainerHandler implements Command.Handler<AddTrainerCommand, UUID>
{
    private final UsersGateway userGateway;
    private final CurrentUserProvider currentUserProvider;
    private final DomainEventsHandler domainEventsHandler;

    public AddTrainerHandler(
            UsersGateway userGateway,
            CurrentUserProvider currentUserProvider,
            DomainEventsHandler domainEventsHandler) {
        this.userGateway = userGateway;
        this.currentUserProvider = currentUserProvider;
        this.domainEventsHandler = domainEventsHandler;
    }

    @Override
    public UUID handle(AddTrainerCommand command) {
        var commandUserId = command.userId();
        var currentUser = currentUserProvider.getCurrentUser();
        var currentUserId = currentUser.id();

        if (!commandUserId.equals(currentUserId))
            throw new UnauthorizedException("The user id of the request " + commandUserId + " does " +
                    "not match the user id of the token " + currentUserId);

        var userId = command.userId();
        var user = userGateway.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                UserErrors.notFound(userId),
                                UserErrors.NOT_FOUND));

        var participantId = user.createTrainerProfile();
        userGateway.update(user);
        domainEventsHandler.publishEvents(user);

        return participantId;
    }
}
