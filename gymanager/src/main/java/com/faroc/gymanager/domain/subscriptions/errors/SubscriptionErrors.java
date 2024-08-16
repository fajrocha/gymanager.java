package com.faroc.gymanager.domain.subscriptions.errors;

import java.util.UUID;

public class SubscriptionErrors {
    public static final String NOT_FOUND = "Subscription not found.";
    public static String notFound(UUID subscriptionId) {
        return "Subscription id " + subscriptionId + "not found to complete request.";
    }

    public static final String CONFLICT_GYM = "Gym already exists on this subscription.";
    public static String conflictGym(UUID gymId, UUID subscriptionId) {
        return "Gym with id " + gymId + "already exists on subscription " + subscriptionId + ".";
    }

    public static final String MAX_GYMS_REACHED = "Maximum gyms reached for this subscription.";
    public static String maxGymsReached(UUID subscriptionId) {
      return "Maximum gyms reached for subscription with id " + subscriptionId + ".";
    }
}
