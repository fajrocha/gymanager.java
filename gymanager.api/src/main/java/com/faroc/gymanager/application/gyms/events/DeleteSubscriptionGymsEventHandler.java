package com.faroc.gymanager.application.gyms.events;

import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.domain.admins.events.SubscriptionDeletedEvent;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DeleteSubscriptionGymsEventHandler {
    private final GymsGateway gymsGateway;

    public DeleteSubscriptionGymsEventHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @TransactionalEventListener
    @Async
    public void handle(SubscriptionDeletedEvent subscriptionDeletedEvent) {
        var subscriptionId = subscriptionDeletedEvent.subscriptionId();
        try {
            gymsGateway.deleteBySubscription(subscriptionId);
        } catch (Exception ex) {
            throw new EventualConsistencyException("Failed to delete gyms for subscription " + subscriptionId);
        }
    }
}
