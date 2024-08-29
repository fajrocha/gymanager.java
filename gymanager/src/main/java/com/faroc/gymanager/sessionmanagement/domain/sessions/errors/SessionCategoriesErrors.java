package com.faroc.gymanager.sessionmanagement.domain.sessions.errors;

import java.util.UUID;

public class SessionCategoriesErrors {
    public static final String GYM_NOT_FOUND = "Gym not found to add session categories.";

    public static String gymNotFound(UUID gymId) {
        return "Gym with id " + gymId + " not found to add session categories.";
    }
}
