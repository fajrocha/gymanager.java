package com.faroc.gymanager.gymmanagement.unit.domain.rooms.utils;


import com.faroc.gymanager.gymmanagement.domain.rooms.Room;

import java.util.UUID;

public class RoomsTestsFactory {
    public static final int DEFAULT_MAX_SESSIONS = 1;
    public static final String DEFAULT_NAME = "Super Room";

    public static Room create() {
        return new Room(UUID.randomUUID(), DEFAULT_NAME, DEFAULT_MAX_SESSIONS);
    }
}
