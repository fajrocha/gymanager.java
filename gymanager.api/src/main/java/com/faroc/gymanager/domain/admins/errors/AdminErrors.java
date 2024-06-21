package com.faroc.gymanager.domain.admins.errors;

import java.util.UUID;

public class AdminErrors {
    public static final String NOT_FOUND = "Admin profile not found.";
    public static String notFound(UUID adminId) {
        return "Admin profile with id " + adminId + "not found to complete request.";
    }

    public static final String CONFLICT_SUBSCRIPTION = "Admin already has a subscription.";
    public static String conflictSubscription(UUID adminId) {
        return "Admin with id " + adminId + "already has a subscription.";
    }

    public static final String SUBSCRIPTION_ID_NOT_MATCHING
            = "Subscription id given does not match with user subscription.";

    public static String subscriptionIdNotMatching(UUID subscriptionId, UUID userId) {
        return "Deleting subscription failed. Subscription given " + subscriptionId + " for user " + userId +
                " does not match with user subscription.";
    }
}
