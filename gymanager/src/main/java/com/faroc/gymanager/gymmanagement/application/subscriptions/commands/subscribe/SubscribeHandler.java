package com.faroc.gymanager.gymmanagement.application.subscriptions.commands.subscribe;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.gymmanagement.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.gymmanagement.domain.admins.errors.AdminErrors;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SubscribeHandler implements Command.Handler<SubscribeCommand, Subscription>{
    private final AdminsGateway adminsGateway;
    private final DomainEventsPublisher domainEventsPublisher;

    public SubscribeHandler(
            AdminsGateway adminsGateway, DomainEventsPublisher domainEventsPublisher) {
        this.adminsGateway = adminsGateway;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    @Transactional
    public Subscription handle(SubscribeCommand command) {
        var adminId = command.adminId();

        var admin = adminsGateway.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AdminErrors.notFound(adminId),
                        AdminErrors.NOT_FOUND));

        var subscription = new Subscription(
                adminId,
                command.subscriptionType()
        );

        admin.setSubscription(subscription);
        adminsGateway.update(admin);

        domainEventsPublisher.publishEventsFromAggregate(admin);

        return subscription;
    }
}
