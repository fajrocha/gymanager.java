package com.faroc.gymanager.application.subscriptions.events;

import an.awesome.pipelinr.Notification;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.domain.admins.events.SubscriptionDeletedEvent;
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors;
import org.springframework.stereotype.Component;

@Component
public class DeleteSubscriptionEventHandler implements Notification.Handler<SubscriptionDeletedEvent> {
    private final SubscriptionsGateway subscriptionsGateway;

    public DeleteSubscriptionEventHandler(SubscriptionsGateway subscriptionsGateway) {
        this.subscriptionsGateway = subscriptionsGateway;
    }

    @Override
    public void handle(SubscriptionDeletedEvent subscriptionDeletedEvent) {
        subscriptionsGateway.delete(subscriptionDeletedEvent.subscriptionId());
    }
}
