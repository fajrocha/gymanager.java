package unit.domain.gyms.utils;

import com.faroc.gymanager.domain.gyms.Gym;

import java.util.UUID;

public class GymsTestsFactory {
    public static Gym create() {
        return new Gym(
                UUID.randomUUID(),
                UUID.randomUUID(),
                GymConstants.GYM_NAME_DEFAULT,
                Integer.MAX_VALUE
        );
    }

    public static Gym create(int maxRooms) {
        return new Gym(
                UUID.randomUUID(),
                GymConstants.GYM_NAME_DEFAULT,
                maxRooms
        );
    }

    public static Gym create(UUID id) {
        return new Gym(
                id,
                UUID.randomUUID(),
                GymConstants.GYM_NAME_DEFAULT,
                Integer.MAX_VALUE
        );
    }

    public static Gym create(UUID id, String sessionCategory) {
        var gym = new Gym(
                id,
                UUID.randomUUID(),
                GymConstants.GYM_NAME_DEFAULT,
                Integer.MAX_VALUE
        );

        gym.addCategory(sessionCategory);

        return gym;
    }
}
