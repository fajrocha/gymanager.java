package com.faroc.gymanager.application.subscriptions.events;

import an.awesome.pipelinr.Notification;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.domain.admins.events.SubscriptionDeletedEvent;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DeleteSubscriptionEventHandler {
    private final SubscriptionsGateway subscriptionsGateway;

    public DeleteSubscriptionEventHandler(SubscriptionsGateway subscriptionsGateway) {
        this.subscriptionsGateway = subscriptionsGateway;
    }

    @TransactionalEventListener
    @Async
    public void handle(SubscriptionDeletedEvent subscriptionDeletedEvent) {
        var subscriptionId = subscriptionDeletedEvent.subscriptionId();

        try {
            Thread.sleep(10_000);
            subscriptionsGateway.delete(subscriptionId);
        } catch (Exception ex) {
            throw new EventualConsistencyException("Error while deleting subscription with id " + subscriptionId);
        }
    }
}
