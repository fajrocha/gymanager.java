package com.faroc.gymanager.gymmanagement.domain.admins.events;

import com.faroc.gymanager.common.domain.events.DomainEvent;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;

public record SubscriptionCreatedEvent(Subscription subscription) implements DomainEvent {
}
