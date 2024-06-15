package com.faroc.gymanager.domain.subscriptions;

import java.util.UUID;

public class SubscriptionErrors {
    public static final String CONFLICT_GYM = "Gym already exists on this subscription.";
    public static String conflictGym(UUID gymId, UUID subscriptionId) {
        return "Gym with id " + gymId + "already exists on subscription " + subscriptionId + ".";
    }

    public static final String ADMIN_NOT_FOUND = "Admin id not found.";
    public static String adminNotFound(UUID adminId) {
        return "Admin id " + adminId + "not found to complete request.";
    }
}
