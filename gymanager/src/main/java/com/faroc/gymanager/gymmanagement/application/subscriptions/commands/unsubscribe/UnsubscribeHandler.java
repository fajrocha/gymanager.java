package com.faroc.gymanager.gymmanagement.application.subscriptions.commands.unsubscribe;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.gymmanagement.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.gymmanagement.domain.admins.errors.AdminErrors;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UnsubscribeHandler implements Command.Handler<UnsubscribeCommand, Voidy> {
    private final AdminsGateway adminsGateway;
    private final SubscriptionsGateway subscriptionsGateway;
    private final DomainEventsPublisher domainEventsPublisher;

    public UnsubscribeHandler(
            AdminsGateway adminsGateway,
            SubscriptionsGateway subscriptionsGateway,
            DomainEventsPublisher domainEventsPublisher) {
        this.adminsGateway = adminsGateway;
        this.subscriptionsGateway = subscriptionsGateway;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    @Transactional
    public Voidy handle(UnsubscribeCommand unsubscribeCommand) {
        var subscriptionId = unsubscribeCommand.subscriptionId();
        var subscription = subscriptionsGateway.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SubscriptionErrors.notFound(subscriptionId),
                        SubscriptionErrors.NOT_FOUND
                ));

        var adminId = subscription.getAdminId();
        var admin = adminsGateway.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AdminErrors.notFound(adminId),
                        AdminErrors.NOT_FOUND
                ));

        if (admin.getSubscriptionId() == null)
            throw new UnexpectedException(
                    AdminErrors.notFound(adminId),
                    AdminErrors.SUBSCRIPTION_NOT_FOUND
            );

        admin.unsubscribe(subscriptionId);
        adminsGateway.update(admin);

        domainEventsPublisher.publishEventsFromAggregate(admin);

        return new Voidy();
    }
}
