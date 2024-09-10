package com.faroc.gymanager.gymmanagement.domain.gyms.errors;

import java.util.UUID;

public class GymsErrors {
    public static final String MAX_ROOMS_REACHED = "Reached the maximum amount of rooms for this gym.";

    public static String maxRoomsReached(UUID roomId, UUID gymId) {
        return "Failed to add room " + roomId + " to gym " + gymId +
                ". Max rooms reached for this gym.";
    }

    public static final String NOT_FOUND = "Gym not found.";
    public static final String NOT_FOUND_ON_SUBSCRIPTION = "Gym not found for given subscription.";
    public static String notFound(UUID gymId, UUID subscriptionId) {
        return "Gym with id " + gymId + " not found for subscription " + subscriptionId + ".";
    }

    public static final String SUBSCRIPTION_MISMATCH = "Subscription given does not match gym subscription.";
    public static String subscriptionMismatch(UUID gymId, UUID givenSubscriptionId, UUID expectedSubscriptionId) {
        return "Subscription id given (" + givenSubscriptionId + ") for gym with id " + gymId + "does not match the " +
                "gym's subscription id (" + expectedSubscriptionId + ").";
    }

    public static final String CONFLICT_ROOM = "Room is already assigned to this gym.";
    public static String conflictRoom(UUID roomId, UUID gymId) {
        return "Room " + roomId + " is already assigned to the gym " + gymId + ".";
    }

    public static final String CONFLICT_TRAINER = "Trainer is already assigned to this gym.";
    public static String conflictTrainer(UUID trainerId, UUID gymId) {
        return "Trainer " + trainerId + " is already assigned to the gym " + gymId + ".";
    }

    public static String conflictSessionCategory(String sessionCategory) {
        return "Session category " + sessionCategory + " is already assigned to this gym.";
    }

    public static String conflictSessionCategory(String sessionCategory, UUID gymId) {
        return "Session category " + sessionCategory + " is already assigned to the gym " + gymId + ".";
    }
}
