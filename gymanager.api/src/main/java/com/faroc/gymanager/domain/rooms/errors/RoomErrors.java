package com.faroc.gymanager.domain.rooms.errors;

import java.util.UUID;

public class RoomErrors {
    public static final String MAX_SESSIONS_REACHED = "Maximum participants reached for this room.";

    public static String maxSessionsReached(UUID sessionId, UUID roomId) {
        return "Failed to add session " + sessionId + " to room " + roomId +
                ". Max sessions reached for this room.";
    }

    public static final String CONFLICT_SESSION = "Session already exists on this room.";
    public static String conflictGym(UUID sessionId, UUID roomId) {
        return "Session with id " + sessionId + "already exists on room " + roomId + ".";
    }
}
