package com.faroc.gymanager.gymmanagement.api.rooms.mappers;

import com.faroc.gymanager.gymmanagement.domain.rooms.Room;
import com.faroc.gymanager.gymmanagement.rooms.responses.RoomResponse;

public class RoomResponseMappers {
    public static RoomResponse toResponse(Room room) {
        return new RoomResponse(room.getId(), room.getName());
    }
}
