package com.faroc.gymanager.usersmanagement.integration.users.utils;

import java.util.UUID;

public class UsersHttpEndpoints {
    public static String getAdminProfileEndpoint(UUID userId) {
        return getProfileEndpoint(userId, "admin");
    }
    public static String getProfileEndpoint(UUID userId, String profile) {
        return "users/" + userId + "/profiles/" + profile;
    }
}
