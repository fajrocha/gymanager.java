package com.faroc.gymanager.application.subscriptions.commands.createsubscription;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.domain.admins.errors.AdminErrors;
import com.faroc.gymanager.domain.subscriptions.Subscription;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateSubscriptionHandler implements Command.Handler<CreateSubscriptionCommand, Subscription>{
    private final AdminsGateway adminsGateway;
    private final ApplicationEventPublisher eventPublisher;

    public CreateSubscriptionHandler(
            AdminsGateway adminsGateway, ApplicationEventPublisher publisher) {
        this.adminsGateway = adminsGateway;
        this.eventPublisher = publisher;
    }

    @Override
    @Transactional
    public Subscription handle(CreateSubscriptionCommand createSubscriptionCommand) {
        var adminId = createSubscriptionCommand.adminId();

        var admin = adminsGateway.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AdminErrors.notFound(adminId),
                        AdminErrors.NOT_FOUND));

        var subscription = new Subscription(
                adminId,
                createSubscriptionCommand.subscriptionType()
        );

        admin.setSubscription(subscription);
        adminsGateway.update(admin);

        while (admin.hasDomainEvents()) {
            eventPublisher.publishEvent(admin.popEvent());
        }

        return subscription;
    }
}
