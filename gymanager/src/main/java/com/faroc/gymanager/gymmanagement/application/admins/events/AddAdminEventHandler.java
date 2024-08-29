package com.faroc.gymanager.gymmanagement.application.admins.events;

import com.faroc.gymanager.gymmanagement.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.gymmanagement.domain.admins.Admin;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import com.faroc.gymanager.usermanagement.domain.users.events.AddAdminEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class AddAdminEventHandler {
    private final AdminsGateway adminsGateway;

    public AddAdminEventHandler(AdminsGateway adminsGateway) {
        this.adminsGateway = adminsGateway;
    }

    @ApplicationModuleListener
    public void handle(AddAdminEvent addAdminEvent) {
        var adminId = addAdminEvent.adminId();
        var userId = addAdminEvent.userId();
        var admin = new Admin(adminId, userId);

        try {
            adminsGateway.create(admin);
        } catch (Exception ex) {
            throw new EventualConsistencyException(
                    "Failed to add admin with id " + adminId + " to user with id" + userId + ".");
        }
    }
}
