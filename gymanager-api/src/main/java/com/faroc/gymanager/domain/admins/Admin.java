package com.faroc.gymanager.domain.admins;

import an.awesome.pipelinr.Notification;
import com.faroc.gymanager.domain.admins.errors.AdminErrors;
import com.faroc.gymanager.domain.admins.events.SubscriptionDeletedEvent;
import com.faroc.gymanager.domain.admins.exceptions.SubscriptionIdNotMatching;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Admin {
    private final UUID id;
    private final UUID userId;
    private UUID subscriptionId;
    private final List<Notification> domainEvents = new ArrayList<>();

    public Admin(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
    }

    public Admin(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
    }

    private Admin(UUID id, UUID userId, UUID subscriptionId) {
        this.id = id;
        this.userId = userId;
        this.subscriptionId = subscriptionId;
    }

    public static Admin MapFromStorage(UUID id, UUID userId, UUID subscriptionId) {
        return new Admin(id, userId, subscriptionId);
    }

    public void setSubscription(UUID subscriptionId) {
        if (this.subscriptionId != null)
            throw new ConflictException(
                    AdminErrors.conflictSubscription(id),
                    AdminErrors.CONFLICT_SUBSCRIPTION
            );

        this.subscriptionId = subscriptionId;
    }

    public void deleteSubscription(UUID subscriptionId) {
        if (!this.subscriptionId.equals(subscriptionId))
            throw new SubscriptionIdNotMatching(
                    "Deleting subscription failed. Subscription given " + subscriptionId + " for user " + id +
                            " does not match with user subscription.");

        domainEvents.add(new SubscriptionDeletedEvent(subscriptionId));

        this.subscriptionId = null;
    }
}
