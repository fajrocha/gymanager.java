package com.faroc.gymanager.application.gyms.events;

import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.domain.admins.events.SubscriptionDeletedEvent;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class DeleteSubscriptionGymsEventHandler {
    private final GymsGateway gymsGateway;

    public DeleteSubscriptionGymsEventHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @ApplicationModuleListener
    public void handle(SubscriptionDeletedEvent subscriptionDeletedEvent) {
        var subscriptionId = subscriptionDeletedEvent.subscriptionId();
        try {
            gymsGateway.deleteBySubscription(subscriptionId);
        } catch (Exception ex) {
            throw new EventualConsistencyException("Failed to delete gyms for subscription " + subscriptionId);
        }
    }
}
