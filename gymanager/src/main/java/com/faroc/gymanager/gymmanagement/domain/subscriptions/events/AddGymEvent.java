package com.faroc.gymanager.gymmanagement.domain.subscriptions.events;

import com.faroc.gymanager.common.domain.events.DomainEvent;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;

import java.util.UUID;

public record AddGymEvent(Subscription subscription, Gym gym) implements DomainEvent {
    public static String failedToAddGym(UUID subscriptionId, UUID gymId) {
        return "Failed to add gym with id " + gymId + " to subscription with id " + subscriptionId + ".";
    }
}
