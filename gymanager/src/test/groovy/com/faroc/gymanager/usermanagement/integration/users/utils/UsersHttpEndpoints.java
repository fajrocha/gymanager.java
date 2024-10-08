package com.faroc.gymanager.usermanagement.integration.users.utils;

import java.util.UUID;

public class UsersHttpEndpoints {
    public static String getAdminProfileEndpoint(UUID userId) {
        return getProfileEndpoint(userId, "admin");
    }
    public static String getProfileEndpoint(UUID userId, String profile) {
        return "v1/users/" + userId + "/profiles/" + profile;
    }
}
