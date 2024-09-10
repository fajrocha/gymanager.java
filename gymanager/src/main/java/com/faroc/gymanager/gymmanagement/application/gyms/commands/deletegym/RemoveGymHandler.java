package com.faroc.gymanager.gymmanagement.application.gyms.commands.deletegym;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.gymmanagement.domain.gyms.errors.GymsErrors;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveGymHandler implements Command.Handler<RemoveGymCommand, Voidy> {
    private final SubscriptionsGateway subscriptionsGateway;
    private final GymsGateway gymsGateway;
    private final DomainEventsPublisher domainEventsPublisher;

    public RemoveGymHandler(
            SubscriptionsGateway subscriptionsGateway,
            GymsGateway gymsGateway,
            DomainEventsPublisher domainEventsPublisher) {
        this.subscriptionsGateway = subscriptionsGateway;
        this.gymsGateway = gymsGateway;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    @Transactional
    public Voidy handle(RemoveGymCommand removeGymCommand) {
        var gymId = removeGymCommand.gymId();
        var subscriptionId = removeGymCommand.subscriptionId();

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

        subscription.removeGym(gym);

        subscriptionsGateway.update(subscription);
        domainEventsPublisher.publishEventsFromAggregate(subscription);

        return new Voidy();
    }
}
