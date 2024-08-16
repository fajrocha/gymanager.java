package com.faroc.gymanager.integration.gyms.utils;

import java.util.UUID;

public class EndpointsGyms {
    public static String getAddGymEndpoint(UUID subscriptionId) {
        return "subscriptions/" + subscriptionId + "/gyms";
    }
}
