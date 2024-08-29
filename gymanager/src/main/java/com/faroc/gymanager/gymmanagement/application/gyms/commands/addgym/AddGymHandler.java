package com.faroc.gymanager.gymmanagement.application.gyms.commands.addgym;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddGymHandler implements Command.Handler<AddGymCommand, Gym> {
    private final SubscriptionsGateway subscriptionsGateway;
    private final DomainEventsPublisher domainEventsPublisher;

    public AddGymHandler(SubscriptionsGateway subscriptionsGateway, DomainEventsPublisher domainEventsPublisher) {
        this.subscriptionsGateway = subscriptionsGateway;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    @Transactional
    public Gym handle(AddGymCommand addGymCommand) {
        var subscriptionId = addGymCommand.subscriptionId();
        var subscription = subscriptionsGateway.findById(subscriptionId)
                .orElseThrow(() -> new UnexpectedException(
                        SubscriptionErrors.notFound(subscriptionId),
                        SubscriptionErrors.NOT_FOUND));

        var gym = new Gym(subscriptionId, addGymCommand.name(), subscription.getMaxRooms());
        subscription.addGym(gym);
        subscriptionsGateway.update(subscription);
        domainEventsPublisher.publishEventsFromAggregate(subscription);

        return gym;
    }
}
