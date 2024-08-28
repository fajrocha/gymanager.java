package com.faroc.gymanager.gymmanagement.application.gyms.events;

import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.events.AddGymEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class AddGymEventHandler {
    private final GymsGateway gymsGateway;

    @Autowired
    public AddGymEventHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @ApplicationModuleListener
    public void handle(AddGymEvent event) {
        var subscription = event.subscription();
        var subscriptionId = subscription.getId();
        var gym = event.gym();
        var gymId = gym.getId();
        var gymToAdd = new Gym(gymId ,subscriptionId, gym.getName(), subscription.getMaxRooms());

        try {
            gymsGateway.save(gymToAdd);
        } catch (RuntimeException ex) {
            throw new EventualConsistencyException(AddGymEvent.failedToAddGym(subscriptionId, gymId), ex);
        }
    }
}
