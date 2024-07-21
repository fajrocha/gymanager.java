package com.faroc.gymanager.api.gyms.mappers;

import com.faroc.gymanager.domain.gyms.Gym;
import com.faroc.gymanager.gyms.responses.GymResponse;

import java.util.List;

public class GymResponseMappers {
    public static GymResponse toResponse(Gym gym) {
        return new GymResponse(
                gym.getId(),
                gym.getName(),
                gym.getSessionCategories().stream().toList()
        );
    }

    public static List<GymResponse> toResponse(List<Gym> gyms) {
        return gyms.stream()
                .map(gym -> new GymResponse(
                        gym.getId(),
                        gym.getName(),
                        gym.getSessionCategories().stream().toList()))
                .toList();
    }
}
