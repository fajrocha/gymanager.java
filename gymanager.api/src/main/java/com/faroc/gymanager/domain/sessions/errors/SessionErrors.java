package com.faroc.gymanager.domain.sessions.errors;

import java.util.UUID;

public class SessionErrors {
    public final static String MAX_PARTICIPANTS_REACHED = "Maximum participants reached for this session.";
    public static String maxParticipantsReached(UUID sessionId, UUID participantId) {
        return "Failed to add participant " + participantId + " to session " + sessionId + ".";
    }

    public final static String CANCELLATION_CLOSE_TO_START = "Cancellation request is too close to the session start time.";
    public static String cancellationCloseToStart(UUID sessionId, UUID participantId) {
        return "Failed to cancel session for participant " + participantId + " to session " + sessionId + ". " +
                "Too close to start time";
    }

    public final static String PARTICIPANT_NOT_FOUND = "Participant not found for this session.";

    public static String participantNotFound(UUID sessionId, UUID participantId) {
        return "Failed to cancel session for participant " + participantId + " to session " + sessionId + ". " +
                "Participant was not found for this session.";
    }

}
