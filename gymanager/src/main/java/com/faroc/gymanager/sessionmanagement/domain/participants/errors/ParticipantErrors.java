package com.faroc.gymanager.sessionmanagement.domain.participants.errors;

import java.util.UUID;

public class ParticipantErrors {
    public static final String CONFLICT_SESSION = "Session reservation already exists for this participant.";
    public static String conflictSession(UUID sessionId, UUID participantId) {
        return "Session reservation with id " + sessionId + "already exists for participant " + participantId + ".";
    }
    public static final String SESSION_NOT_FOUND = "Session reservation not found for this participant.";
    public static String sessionNotFound(UUID sessionId, UUID participantId) {
        return "Session reservation with id " + sessionId + " not found for participant " + participantId + ".";
    }
}
