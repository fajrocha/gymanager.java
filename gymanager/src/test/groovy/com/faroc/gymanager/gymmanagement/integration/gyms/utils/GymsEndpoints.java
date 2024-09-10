package com.faroc.gymanager.gymmanagement.integration.gyms.utils;

import java.util.UUID;

public class GymsEndpoints {
    public static String getAddGymEndpointV1(UUID subscriptionId) {
        return "v1/subscriptions/" + subscriptionId + "/gyms";
    }
}
