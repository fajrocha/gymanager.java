package com.faroc.gymanager.sessionmanagement.domain.rooms.events;

import com.faroc.gymanager.sessionmanagement.domain.rooms.Room;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.common.domain.events.DomainEvent;

import java.util.UUID;

public record SessionReservationEvent(Room room, Session session) implements DomainEvent {
    public static String sessionAddFailed(UUID sessionId) {
        return "Failed to add session with id " + sessionId + ".";
    }

    public static String trainerNotFound(UUID trainerId, UUID sessionId) {
        return "Trainer with id " + trainerId + " not found to make reservation on session with id " + sessionId + ".";
    }

    public static String trainerUpdateFailed(UUID trainerId, UUID sessionId) {
        return "Failed to update trainer with id " + trainerId + " with session with id " + sessionId + ".";
    }

    public static String gymNotFound(UUID gymId, UUID trainerId) {
        return "Trainer with id " + gymId + " not found to add trainer with id " + trainerId + ".";
    }
}
