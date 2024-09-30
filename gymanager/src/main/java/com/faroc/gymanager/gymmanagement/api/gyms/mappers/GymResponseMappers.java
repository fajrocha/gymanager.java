package com.faroc.gymanager.gymmanagement.api.gyms.mappers;

import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import com.faroc.gymanager.gymmanagement.api.gyms.contracts.v1.responses.GymResponse;

import java.util.List;

public class GymResponseMappers {
    public static GymResponse toResponse(Gym gym) {
        return new GymResponse(
                gym.getId(),
                gym.getName()
        );
    }

    public static List<GymResponse> toResponse(List<Gym> gyms) {
        return gyms.stream()
                .map(gym -> new GymResponse(
                        gym.getId(),
                        gym.getName()))
                .toList();
    }
}
