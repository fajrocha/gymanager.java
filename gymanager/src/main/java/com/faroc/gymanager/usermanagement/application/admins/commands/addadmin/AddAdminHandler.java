package com.faroc.gymanager.usermanagement.application.admins.commands.addadmin;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.common.application.security.CurrentUserProvider;
import com.faroc.gymanager.common.application.security.exceptions.UnauthorizedException;
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.usermanagement.application.users.gateways.UsersGateway;
import com.faroc.gymanager.usermanagement.domain.users.errors.UserErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class AddAdminHandler implements Command.Handler<AddAdminCommand, UUID> {
    private final UsersGateway userGateway;
    private final CurrentUserProvider currentUserProvider;
    private final DomainEventsPublisher domainEventsHandler;

    public AddAdminHandler(
            UsersGateway userGateway,
            CurrentUserProvider currentUserProvider,
            DomainEventsPublisher domainEventsHandler) {
        this.userGateway = userGateway;
        this.currentUserProvider = currentUserProvider;
        this.domainEventsHandler = domainEventsHandler;
    }

    @Override
    @Transactional
    public UUID handle(AddAdminCommand command) {
        var commandUserId = command.userId();
        var currentUser = currentUserProvider.getCurrentUser();
        var currentUserId = currentUser.id();

        if (!commandUserId.equals(currentUserId))
            throw new UnauthorizedException("The user id of the request " + commandUserId + " does " +
                    "not match the user id of the token " + currentUserId);

        var userId = command.userId();
        var user = userGateway.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(UserErrors.notFound(userId), UserErrors.NOT_FOUND));

        var adminId = user.createAdminProfile();
        userGateway.update(user);
        domainEventsHandler.publishEventsFromAggregate(user);

        return adminId;
     }
}
