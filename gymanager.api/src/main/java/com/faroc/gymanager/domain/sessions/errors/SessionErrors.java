package com.faroc.gymanager.domain.sessions.errors;

import java.util.UUID;

public class SessionErrors {
    public static final String MAX_PARTICIPANTS_REACHED = "Maximum participants reached for this session.";
    public static String maxParticipantsReached(UUID sessionId, UUID participantId) {
        return "Failed to add participant " + participantId + " to session " + sessionId +
                ". Maximum participants reached for this session.";
    }

    public static final String CONFLICT_PARTICIPANT = "Participant already has reservation on this session.";
    public static String conflictParticipant(UUID sessionId, UUID participantId) {
        return "Participant " + participantId + "  already has reservation on session " + sessionId + ".";
    }

    public static final String CANCELLATION_CLOSE_TO_START = "Cancellation request is too close to the session start time.";
    public static String cancellationCloseToStart(UUID sessionId, UUID participantId) {
        return "Failed to cancel session for participant " + participantId + " to session " + sessionId + ". " +
                "Too close to start time";
    }

    public static final String PARTICIPANT_NOT_FOUND = "Participant not found for this session.";

    public static String participantNotFound(UUID sessionId, UUID participantId) {
        return "Failed to cancel session for participant " + participantId + " to session " + sessionId + ". " +
                "Participant was not found for this session.";
    }

}
