package com.faroc.gymanager.sessionmanagement.unit.application.gyms.utils;

import com.faroc.gymanager.sessionmanagement.domain.gyms.Gym;

import java.util.UUID;

public class GymsTestsFactory {
    public static Gym create(UUID id) {
        return new Gym(id);
    }

    public static Gym create(UUID id, String sessionCategory) {
        var gym = new Gym(id);

        gym.addCategory(sessionCategory);

        return gym;
    }
}
