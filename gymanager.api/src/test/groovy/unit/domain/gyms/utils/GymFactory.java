package unit.domain.gyms.utils;

import com.faroc.gymanager.domain.gyms.Gym;
import com.faroc.gymanager.domain.gyms.errors.GymsErrors;

import java.util.UUID;

public class GymFactory {
    public static Gym create(int maxRooms) {
        return new Gym(
                UUID.randomUUID(),
                GymConstants.GYM_NAME_DEFAULT,
                maxRooms
        );
    }
}
