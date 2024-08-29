package com.faroc.gymanager.gymmanagement.integration.gyms.utils;

import java.util.UUID;

public class GymsEndpoints {
    public static String getAddGymEndpoint(UUID subscriptionId) {
        return "subscriptions/" + subscriptionId + "/gyms";
    }
}
