package com.faroc.gymanager.application.gyms.events;

import an.awesome.pipelinr.Notification;
import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.domain.admins.events.SubscriptionDeletedEvent;
import org.springframework.stereotype.Component;

@Component
public class DeleteSubscriptionGymsEventHandler implements Notification.Handler<SubscriptionDeletedEvent> {
    private final GymsGateway gymsGateway;

    public DeleteSubscriptionGymsEventHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @Override
    public void handle(SubscriptionDeletedEvent subscriptionDeletedEvent) {
        gymsGateway.deleteBySubscriptionId(subscriptionDeletedEvent.subscriptionId());
    }
}
