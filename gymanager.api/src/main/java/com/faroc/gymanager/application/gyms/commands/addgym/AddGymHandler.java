package com.faroc.gymanager.application.gyms.commands.addgym;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.application.security.authorization.Authorize;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.domain.gyms.Gym;
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddGymHandler implements Command.Handler<AddGymCommand, Gym> {
    private final SubscriptionsGateway subscriptionsGateway;
    private final GymsGateway gymsGateway;

    public AddGymHandler(SubscriptionsGateway subscriptionsGateway, GymsGateway gymsGateway) {
        this.subscriptionsGateway = subscriptionsGateway;
        this.gymsGateway = gymsGateway;
    }

    @Override
    @Transactional
    public Gym handle(AddGymCommand addGymCommand) {
        var subscriptionId = addGymCommand.subscriptionId();
        var subscription = subscriptionsGateway.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SubscriptionErrors.notFound(subscriptionId),
                        SubscriptionErrors.NOT_FOUND));

        var gym = new Gym(subscriptionId, addGymCommand.name(), subscription.getMaxRooms());
        gymsGateway.save(gym);

        subscription.addGym(gym.getId());
        subscriptionsGateway.update(subscription);

        return gym;
    }
}
