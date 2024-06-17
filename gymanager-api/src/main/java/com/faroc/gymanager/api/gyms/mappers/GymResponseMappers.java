package com.faroc.gymanager.api.gyms.mappers;

import com.faroc.gymanager.domain.gyms.Gym;
import com.faroc.gymanager.gyms.requests.GymResponse;

public class GymResponseMappers {

    public static GymResponse toResponse(Gym gym) {
        return new GymResponse(
                gym.getId(),
                gym.getSubscriptionId(),
                gym.getName()
        );
    }
}
