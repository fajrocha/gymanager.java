package com.faroc.gymanager.domain.admins;

import com.faroc.gymanager.domain.shared.AggregateRoot;
import com.faroc.gymanager.domain.shared.abstractions.DomainEventsTracker;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import com.faroc.gymanager.domain.admins.errors.AdminErrors;
import com.faroc.gymanager.domain.admins.events.SubscriptionDeletedEvent;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import lombok.Getter;
import java.util.UUID;

@Getter
public class Admin extends AggregateRoot {
    private final UUID userId;
    private UUID subscriptionId;

    public Admin(UUID userId) {
        this.userId = userId;
    }

    public Admin(UUID id, UUID userId) {
        super(id);
        this.userId = userId;
    }

    private Admin(UUID id, UUID userId, UUID subscriptionId) {
        super(id);
        this.userId = userId;
        this.subscriptionId = subscriptionId;
    }

    public static Admin mapFromStorage(UUID id, UUID userId, UUID subscriptionId) {
        return new Admin(id, userId, subscriptionId);
    }

    public void setSubscription(UUID subscriptionId) {
        if (this.subscriptionId != null)
            throw new ConflictException(
                    AdminErrors.conflictSubscription(id),
                    AdminErrors.CONFLICT_SUBSCRIPTION);

        this.subscriptionId = subscriptionId;
    }

    public void deleteSubscription(UUID subscriptionId) {
        if (!this.subscriptionId.equals(subscriptionId))
            throw new UnexpectedException(
                    AdminErrors.subscriptionIdNotMatching(subscriptionId, userId),
                    AdminErrors.SUBSCRIPTION_ID_NOT_MATCHING);

        domainEvents.add(new SubscriptionDeletedEvent(subscriptionId));

        this.subscriptionId = null;
    }
}
