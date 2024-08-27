package com.faroc.gymanager.gymmanagement.domain.rooms.errors;

import java.util.UUID;

public class RoomErrors {
    public static final String GYM_NOT_FOUND = "Gym not found to add new room.";
    public static String gymNotFound(UUID gymId) {
        return "Gym with id " + gymId + " not found to add new room.";
    }

    public static final String SUBSCRIPTION_NOT_FOUND = "Subscription not found to add new room.";
    public static String subscriptionNotFound(UUID subscriptionId) {
        return "Subscription with id " + subscriptionId + " not found to add new room.";
    }
}
