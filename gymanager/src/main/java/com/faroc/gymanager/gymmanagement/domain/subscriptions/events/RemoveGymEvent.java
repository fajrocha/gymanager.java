package com.faroc.gymanager.gymmanagement.domain.subscriptions.events;

import com.faroc.gymanager.common.domain.events.DomainEvent;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;

import java.util.UUID;

public record RemoveGymEvent(Subscription subscription, Gym gym) implements DomainEvent {
    public static String failedToRemoveGym(UUID subscriptionId, UUID gymId) {
        return "Failed to remove gym with id " + gymId + " from subscription with id " + subscriptionId + ".";
    }
}
