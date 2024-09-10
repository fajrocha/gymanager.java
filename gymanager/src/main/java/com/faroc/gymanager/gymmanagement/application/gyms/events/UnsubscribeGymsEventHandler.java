package com.faroc.gymanager.gymmanagement.application.gyms.events;

import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.gymmanagement.domain.admins.events.UnsubscribeEvent;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class UnsubscribeGymsEventHandler {
    private final GymsGateway gymsGateway;

    public UnsubscribeGymsEventHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @ApplicationModuleListener
    public void handle(UnsubscribeEvent unsubscribeEvent) {
        var subscriptionId = unsubscribeEvent.subscriptionId();
        try {
            gymsGateway.deleteBySubscription(subscriptionId);
        } catch (Exception ex) {
            throw new EventualConsistencyException("Failed to delete gyms for subscription " + subscriptionId);
        }
    }
}
