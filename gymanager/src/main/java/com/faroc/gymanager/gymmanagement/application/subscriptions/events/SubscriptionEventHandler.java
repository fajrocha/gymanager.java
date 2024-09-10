package com.faroc.gymanager.gymmanagement.application.subscriptions.events;

import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.gymmanagement.domain.admins.events.SubscriptionCreatedEvent;
import com.faroc.gymanager.gymmanagement.domain.admins.events.UnsubscribeEvent;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class SubscriptionEventHandler {
    private final SubscriptionsGateway subscriptionsGateway;

    public SubscriptionEventHandler(SubscriptionsGateway subscriptionsGateway) {
        this.subscriptionsGateway = subscriptionsGateway;
    }

    @ApplicationModuleListener
    public void handle(UnsubscribeEvent unsubscribeEvent) {
        var subscriptionId = unsubscribeEvent.subscriptionId();

        try {
            subscriptionsGateway.delete(subscriptionId);
        } catch (Exception ex) {
            throw new EventualConsistencyException("Error while deleting subscription with id " + subscriptionId);
        }
    }

    @TransactionalEventListener
    @Async
    public void handle(SubscriptionCreatedEvent subscriptionDeletedEvent) {
        var subscription = subscriptionDeletedEvent.subscription();

        try {
            subscriptionsGateway.save(subscription);
        } catch (Exception ex) {
            throw new EventualConsistencyException("Error while creating subscription with id " + subscription.getId());
        }
    }
}
