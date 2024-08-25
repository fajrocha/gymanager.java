package com.faroc.gymanager.gymmanagement.domain.admins.events;

import com.faroc.gymanager.common.domain.events.DomainEvent;
import java.util.UUID;

public record SubscriptionDeletedEvent(UUID subscriptionId) implements DomainEvent {
}
