package com.faroc.gymanager.domain.admins.errors;

import java.util.UUID;

public class AdminErrors {
    public static final String NOT_FOUND = "Admin profile not found.";
    public static String notFound(UUID adminId) {
        return "Admin profile with id " + adminId + "not found to complete request.";
    }

    public static final String CREATE_ADMIN_PROFILE = "Failed to create admin profile.";

    public static String createAdminProfile(UUID userId) {
        return "Failed to create admin profile for user " + userId + " .";
    }

    public static final String CONFLICT_SUBSCRIPTION = "Admin already has a subscription.";
    public static String conflictSubscription(UUID adminId) {
        return "Admin with id " + adminId + "already has a subscription.";
    }

    public static final String SUBSCRIPTION_ID_NOT_MATCHING
            = "Subscription id given does not match with user subscription.";
}
