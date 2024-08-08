package unit.domain.rooms.utils;

import com.faroc.gymanager.domain.rooms.Room;

import java.util.UUID;

public class RoomsTestsFactory {
    public static final int DEFAULT_MAX_SESSIONS = 1;
    public static final String DEFAULT_NAME = "Super Room";

    public static Room create() {
        return new Room(UUID.randomUUID(), DEFAULT_NAME, DEFAULT_MAX_SESSIONS);
    }
    public static Room create(UUID id) {
        return new Room(id, DEFAULT_NAME, DEFAULT_MAX_SESSIONS);
    }
    public static Room create(UUID id, UUID gymId) {

        return new Room(id, gymId, DEFAULT_NAME, DEFAULT_MAX_SESSIONS);
    }

    public static Room create(int maxDailySessions) {
        return new Room(UUID.randomUUID(), DEFAULT_NAME, maxDailySessions);
    }
}
