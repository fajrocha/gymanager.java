package unit.domain.rooms.utils;

import com.faroc.gymanager.domain.rooms.Room;

import java.util.UUID;

public class RoomsFactory {
    public static Room create(int maxDailySessions) {
        return new Room(UUID.randomUUID(), maxDailySessions);
    }
}
