package com.faroc.gymanager.application.gyms.commands.deletegym;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.shared.exceptions.UnexpectedException;
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.domain.gyms.errors.GymsErrors;
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.stereotype.Component;

@Component
public class DeleteGymHandler implements Command.Handler<DeleteGymCommand, Voidy> {
    private final SubscriptionsGateway subscriptionsGateway;
    private final GymsGateway gymsGateway;

    public DeleteGymHandler(SubscriptionsGateway subscriptionsGateway, GymsGateway gymsGateway) {
        this.subscriptionsGateway = subscriptionsGateway;
        this.gymsGateway = gymsGateway;
    }

    @Override
    public Voidy handle(DeleteGymCommand deleteGymCommand) {
        var gymId = deleteGymCommand.gymId();
        var subscriptionId = deleteGymCommand.subscriptionId();

        var gym = gymsGateway.findById(gymId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        GymsErrors.notFound(gymId, subscriptionId),
                        GymsErrors.NOT_FOUND
                ));

        var subscription = subscriptionsGateway.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SubscriptionErrors.notFound(subscriptionId),
                        SubscriptionErrors.NOT_FOUND
                ));

        if (!subscription.hasGym(gymId))
            throw new UnexpectedException(
                    GymsErrors.notFound(gymId, subscriptionId),
                    GymsErrors.NOT_FOUND_ON_SUBSCRIPTION);

        subscription.removeGym(gymId);

        subscriptionsGateway.update(subscription);
        gymsGateway.delete(gym);

        return new Voidy();
    }
}
