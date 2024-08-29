package com.faroc.gymanager.gymmanagement.unit.domain.rooms.utils;


import com.faroc.gymanager.gymmanagement.domain.rooms.RoomGym;

import java.util.UUID;

public class RoomsGymTestsFactory {
    public static final int DEFAULT_MAX_SESSIONS = 1;
    public static final String DEFAULT_NAME = "Super Room";

    public static RoomGym create() {
        return new RoomGym(UUID.randomUUID(), DEFAULT_NAME, DEFAULT_MAX_SESSIONS);
    }
    public static RoomGym create(UUID id) {
        return new RoomGym(id, DEFAULT_NAME, DEFAULT_MAX_SESSIONS);
    }
}
