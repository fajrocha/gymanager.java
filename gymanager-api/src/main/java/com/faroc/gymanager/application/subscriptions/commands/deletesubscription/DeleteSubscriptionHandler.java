package com.faroc.gymanager.application.subscriptions.commands.deletesubscription;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import com.faroc.gymanager.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.domain.admins.errors.AdminErrors;
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteSubscriptionHandler implements Command.Handler<DeleteSubscriptionCommand, Voidy> {
    private final Pipeline pipeline;
    private final AdminsGateway adminsGateway;
    private final SubscriptionsGateway subscriptionsGateway;

    public DeleteSubscriptionHandler(
            Pipeline pipeline,
            AdminsGateway adminsGateway,
            SubscriptionsGateway subscriptionsGateway) {
        this.pipeline = pipeline;
        this.adminsGateway = adminsGateway;
        this.subscriptionsGateway = subscriptionsGateway;
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

        admin.deleteSubscription(subscriptionId);

        adminsGateway.update(admin);

        admin.getDomainEvents().forEach(event -> event.send(pipeline));

        return new Voidy();
    }
}
