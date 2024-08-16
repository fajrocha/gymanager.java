package com.faroc.gymanager.domain.admins.events;

import com.faroc.gymanager.domain.shared.events.DomainEvent;
import com.faroc.gymanager.domain.subscriptions.Subscription;

public record SubscriptionCreatedEvent(Subscription subscription) implements DomainEvent {
}
