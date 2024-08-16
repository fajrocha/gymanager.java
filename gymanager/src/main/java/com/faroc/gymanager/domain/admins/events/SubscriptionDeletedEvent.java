package com.faroc.gymanager.domain.admins.events;

import com.faroc.gymanager.domain.shared.events.DomainEvent;
import java.util.UUID;

public record SubscriptionDeletedEvent(UUID subscriptionId) implements DomainEvent {
}
