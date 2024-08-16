package com.faroc.gymanager.application.subscriptions.commands.deletesubscription;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.faroc.gymanager.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.domain.admins.errors.AdminErrors;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteSubscriptionHandler implements Command.Handler<DeleteSubscriptionCommand, Voidy> {
    private final AdminsGateway adminsGateway;
    private final SubscriptionsGateway subscriptionsGateway;
    private final ApplicationEventPublisher eventsPublisher;

    public DeleteSubscriptionHandler(
            AdminsGateway adminsGateway,
            SubscriptionsGateway subscriptionsGateway,
            ApplicationEventPublisher eventsPublisher) {
        this.adminsGateway = adminsGateway;
        this.subscriptionsGateway = subscriptionsGateway;
        this.eventsPublisher = eventsPublisher;
    }

    @Override
    @Transactional
    public Voidy handle(DeleteSubscriptionCommand deleteSubscriptionCommand) {
        var subscriptionId = deleteSubscriptionCommand.subscriptionId();
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

        admin.deleteSubscription(subscriptionId);
        adminsGateway.update(admin);

        while (admin.hasDomainEvents()) {
            eventsPublisher.publishEvent(admin.popEvent());
        }

        return new Voidy();
    }
}
