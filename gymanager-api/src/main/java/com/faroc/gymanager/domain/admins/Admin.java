package com.faroc.gymanager.domain.admins;

import java.util.UUID;

public class Admin {
    private UUID id;
    private UUID userId;
    private UUID subscriptionId;

    public Admin(UUID userId) {
        this.id = UUID.randomUUID();
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
            throw new IllegalArgumentException("Subscription id does not match.");

        this.subscriptionId = null;
    }
}
