package com.faroc.gymanager.application.users.events;

import com.faroc.gymanager.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.domain.admins.Admin;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import com.faroc.gymanager.domain.users.events.AddAdminEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AddAdminEventHandler {
    private final AdminsGateway adminsGateway;

    public AddAdminEventHandler(AdminsGateway adminsGateway) {
        this.adminsGateway = adminsGateway;
    }

    @TransactionalEventListener
    @Async
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
