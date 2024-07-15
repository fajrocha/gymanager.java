package com.faroc.gymanager.api.rooms.mappers;

import com.faroc.gymanager.domain.rooms.Room;
import com.faroc.gymanager.rooms.responses.RoomResponse;

public class RoomResponseMappers {
    public static RoomResponse toResponse(Room room) {
        return new RoomResponse(room.getId(), room.getName());
    }
}
