package com.faroc.gymanager.domain.users.errors;

import java.util.UUID;

public class UserErrors {
    public static final String ADD_USER = "Failed to register user.";
    public static String addUser(String email) {
        return "Failed to add user with given email: " + email + " .";
    }
    public static final String CONFLICT_ADMIN_PROFILE = "User is already an admin.";

    public static String conflictAdminProfile(UUID userId) {
      return "The user " + userId + " already has an admin profile.";
    }

    public static final String CONFLICT_TRAINER_PROFILE = "User is already a trainer.";

    public static String conflictTrainerProfile(UUID userId) {
        return "The user " + userId + " already has a trainer profile.";
    }

    public static final String CONFLICT_PARTICIPANT_PROFILE = "User is already a participant.";

    public static String conflictParticipantProfile(UUID userId) {
        return "The user " + userId + " already has a participant profile.";
    }

    public static final String ADD_ADMIN_PROFILE = "Failed to add admin profile.";

    public static String addAdminProfile(UUID userId) {
        return "Failed to create admin profile for user " + userId + " .";
    }

    public static final String NOT_FOUND = "User not found.";

    public static String notFound(UUID userId) {
        return "User " + userId + " not found.";
    }

    public static String notFound(String userEmail) {
        return "User with email " + userEmail + " not found.";
    }

    public static final String EMAIL_ALREADY_EXISTS = "Email provided is already in use.";

    public static final String authFailed(UUID userId) {
        return "Authentication failed for user " + userId + ".";
    }
}
