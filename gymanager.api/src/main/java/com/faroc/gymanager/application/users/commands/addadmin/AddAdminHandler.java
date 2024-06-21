package com.faroc.gymanager.application.users.commands.addadmin;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.application.security.CurrentUserProvider;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.security.exceptions.UnauthorizedException;
import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.admins.Admin;
import com.faroc.gymanager.domain.users.errors.UserErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class AddAdminHandler implements Command.Handler<AddAdminCommand, UUID> {
    private final UsersGateway userGateway;
    private final AdminsGateway adminsGateway;
    private final CurrentUserProvider currentUserProvider;

    public AddAdminHandler(
            UsersGateway userGateway, 
            AdminsGateway adminsGateway, 
            CurrentUserProvider currentUserProvider) {
        this.userGateway = userGateway;
        this.adminsGateway = adminsGateway;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    @Transactional
    public UUID handle(AddAdminCommand command) {
        var currentUser = currentUserProvider.getCurrentUser();
        var commandUserId = command.userId();
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

        var adminId = user.createAdminProfile();
        var admin = new Admin(adminId, user.getId());

        userGateway.update(user);
        adminsGateway.save(admin);

        return adminId;
    }
}
