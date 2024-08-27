package com.faroc.gymanager.sessionmanagement.domain.gyms.errors;

import java.util.UUID;

public class GymErrors {
    public static String conflictSessionCategory(String sessionCategory) {
        return "Session category " + sessionCategory + " is already assigned to this gym.";
    }

    public static String conflictSessionCategory(String sessionCategory, UUID gymId) {
        return "Session category " + sessionCategory + " is already assigned to the gym " + gymId + ".";
    }
}
