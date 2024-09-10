package com.faroc.gymanager.gymmanagement.application.gyms.events;

import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.events.RemoveGymEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class RemoveGymEventHandler {
    private final GymsGateway gymsGateway;

    @Autowired
    public RemoveGymEventHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @ApplicationModuleListener
    public void handle(RemoveGymEvent event) {
        var gym = event.gym();
        var subscription = event.subscription();

        try {
            gymsGateway.delete(gym);
        } catch (RuntimeException ex) {
            throw new EventualConsistencyException(
                    RemoveGymEvent.failedToRemoveGym(subscription.getId(), gym.getId()),
                    ex);
        }
    }
}
