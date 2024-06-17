package com.faroc.gymanager.domain.gyms.errors;

import java.util.UUID;

public class GymErrors {
    public static String NOT_FOUND = "Gym not found.";
    public static String NOT_FOUND_ON_SUBSCRIPTION = "Gym not found for given subscription.";
    public static String notFound(UUID gymId, UUID subscriptionId) {
        return "Gym with id " + gymId + " not found for subscription " + subscriptionId + ".";
    }
}
