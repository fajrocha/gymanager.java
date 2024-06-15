package com.faroc.gymanager.domain.admins;

import com.faroc.gymanager.domain.admins.exceptions.SubscriptionIdNotMatching;

import java.util.UUID;

public class Admin {
    private final UUID id;
    private final UUID userId;
    private UUID subscriptionId;

    public Admin(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
    }

    public Admin(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscription(UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void deleteSubscription(UUID subscriptionId) {
        if (this.subscriptionId != subscriptionId)
            throw new SubscriptionIdNotMatching(
                    "Deleting subscription failed. Subscription given + " + subscriptionId + " + for user " + id +
                            " does not match with user subscription.");

        this.subscriptionId = null;
    }
}
