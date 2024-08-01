package com.faroc.gymanager.application.trainers.commands.addtrainer;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.security.CurrentUserProvider;
import com.faroc.gymanager.application.security.exceptions.UnauthorizedException;
import com.faroc.gymanager.application.shared.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.users.errors.UserErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class AddTrainerHandler implements Command.Handler<AddTrainerCommand, UUID>
{
    private final UsersGateway userGateway;
    private final CurrentUserProvider currentUserProvider;
    private final DomainEventsPublisher domainEventsPublisher;

    public AddTrainerHandler(
            UsersGateway userGateway,
            CurrentUserProvider currentUserProvider,
            DomainEventsPublisher domainEventsPublisher) {
        this.userGateway = userGateway;
        this.currentUserProvider = currentUserProvider;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    @Transactional
    public UUID handle(AddTrainerCommand command) {
        var commandUserId = command.userId();
        var currentUser = currentUserProvider.getCurrentUser();
        var currentUserId = currentUser.id();

        if (!commandUserId.equals(currentUserId))
            throw new UnauthorizedException("The user id of the request " + commandUserId + " does " +
                    "not match the user id of the token " + currentUserId);

        var userId = command.userId();
        var user = userGateway.findById(userId)
                .orElseThrow(() -> new UnauthorizedException(UserErrors.notFound(userId)));

        var participantId = user.createTrainerProfile();
        userGateway.update(user);
        domainEventsPublisher.publishEventsFromAggregate(user);

        return participantId;
    }
}
