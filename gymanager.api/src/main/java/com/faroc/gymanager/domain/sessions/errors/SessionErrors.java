package com.faroc.gymanager.domain.sessions.errors;

import java.util.UUID;

public class SessionErrors {
    public static String ROOM_NOT_FOUND = "Room was not found.";
    public static String roomNotFound(UUID roomId) {
        return "Failed to add session. Room with id " + roomId + " not found.";
    }
}
