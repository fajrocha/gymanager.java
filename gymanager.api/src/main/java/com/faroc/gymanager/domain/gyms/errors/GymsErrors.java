package com.faroc.gymanager.domain.gyms.errors;

import java.util.UUID;

public class GymsErrors {
    public static final String MAX_ROOMS_REACHED = "Reached the maximum amount of rooms for this gym.";

    public static String maxRoomsReached(UUID roomId, UUID gymId) {
        return "Failed to add room " + roomId + " to gym " + gymId +
                ". Max rooms reached for this gym.";
    }

    public static final String MAX_TRAINERS_REACHED = "Reached the maximum amount of trainers for this gym.";

    public static String maxTrainersReached(UUID trainerId, UUID gymId) {
        return "Failed to add trainer " + trainerId + " to gym " + gymId +
                ". Max trainers reached for this gym.";
    }

    public static final String NOT_FOUND = "Gym not found.";
    public static final String NOT_FOUND_ON_SUBSCRIPTION = "Gym not found for given subscription.";
    public static String notFound(UUID gymId, UUID subscriptionId) {
        return "Gym with id " + gymId + " not found for subscription " + subscriptionId + ".";
    }

    public static final String CONFLICT_ROOM = "Room is already assigned to this gym.";
    public static String conflictRoom(UUID roomId, UUID gymId) {
        return "Room " + roomId + " is already assigned to the gym " + gymId + ".";
    }

    public static final String CONFLICT_TRAINER = "Trainer is already assigned to this gym.";
    public static String conflictTrainer(UUID trainerId, UUID gymId) {
        return "Trainer " + trainerId + " is already assigned to the gym " + gymId + ".";
    }
}
