package com.faroc.gymanager.domain.admins.errors;

import java.util.UUID;

public class AdminErrors {
    public static final String CREATE_ADMIN_PROFILE = "Failed to create admin profile.";

    public static String createAdminProfile(UUID userId) {
        return "Failed to create admin profile for user " + userId + " .";
    }
}
