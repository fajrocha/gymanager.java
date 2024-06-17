package com.faroc.gymanager.application.subscriptions.commands.createsubscription;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.domain.subscriptions.Subscription;
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateSubscriptionHandler implements Command.Handler<CreateSubscriptionCommand, Subscription>{
    private final AdminsGateway adminsGateway;
    private final SubscriptionsGateway subscriptionsGateway;

    public CreateSubscriptionHandler(
            AdminsGateway adminsGateway,
            SubscriptionsGateway subscriptionsGateway) {
        this.adminsGateway = adminsGateway;
        this.subscriptionsGateway = subscriptionsGateway;
    }

    @Override
    @Transactional
    public Subscription handle(CreateSubscriptionCommand createSubscriptionCommand) {
        var adminId = createSubscriptionCommand.adminId();

        var admin = adminsGateway.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SubscriptionErrors.adminNotFound(adminId),
                        SubscriptionErrors.ADMIN_NOT_FOUND));

        var subscription = new Subscription(
                adminId,
                createSubscriptionCommand.subscriptionType()
        );

        admin.setSubscription(subscription.getId());

        subscriptionsGateway.save(subscription);
        adminsGateway.update(admin);

        return subscription;
    }
}
