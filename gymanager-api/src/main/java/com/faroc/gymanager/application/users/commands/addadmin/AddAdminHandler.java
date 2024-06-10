package com.faroc.gymanager.application.users.commands.addadmin;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.application.shared.exceptions.CrudOperationFailed;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.admins.Admin;
import com.faroc.gymanager.domain.admins.errors.AdminErrors;
import com.faroc.gymanager.domain.users.errors.UserErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class AddAdminHandler implements Command.Handler<AddAdminCommand, UUID> {
    private final UsersGateway userGateway;
    private final AdminsGateway adminsGateway;

    public AddAdminHandler(UsersGateway userGateway, AdminsGateway adminsGateway) {
        this.userGateway = userGateway;
        this.adminsGateway = adminsGateway;
    }

    @Override
    @Transactional
    public UUID handle(AddAdminCommand addAdminCommand) {

        var userId = addAdminCommand.userId();
        var user = userGateway.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                UserErrors.notFound(userId),
                                UserErrors.NOT_FOUND));

        var adminId = user.createAdminProfile();
        var admin = new Admin(user.getId());

        var userQueryResult = userGateway.update(user);

        if (userQueryResult == 0)
            throw new CrudOperationFailed(
                    UserErrors.conflictAdminProfile(userId),
                    UserErrors.ADD_ADMIN_PROFILE);

        var adminQueryResult = adminsGateway.save(admin);

        if (adminQueryResult == 0)
            throw new CrudOperationFailed(
                    AdminErrors.createAdminProfile(userId),
                    AdminErrors.CREATE_ADMIN_PROFILE);

        return adminId;
    }
}
