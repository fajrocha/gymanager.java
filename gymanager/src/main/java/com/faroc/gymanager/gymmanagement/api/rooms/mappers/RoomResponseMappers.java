package com.faroc.gymanager.gymmanagement.api.rooms.mappers;

import com.faroc.gymanager.gymmanagement.domain.rooms.RoomGym;
import com.faroc.gymanager.gymmanagement.api.rooms.contracts.v1.responses.RoomResponse;

public class RoomResponseMappers {
    public static RoomResponse toResponse(RoomGym room) {
        return new RoomResponse(room.getId(), room.getName());
    }
}
