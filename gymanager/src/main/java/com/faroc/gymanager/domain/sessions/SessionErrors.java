package com.faroc.gymanager.domain.sessions;

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

    public static final String PARTICIPANT_NOT_FOUND_TO_MAKE_RESERVATION = "Participant not found to make reservation.";

    public static String participantNotFoundToMakeReservation(UUID sessionId, UUID participantId) {
        return "Participant " + participantId + " not found to make a reservation on session " + sessionId + ".";
    }

    public static final String PARTICIPANT_NOT_FREE_TO_MAKE_RESERVATION = "Participant not free to make reservation.";

    public static String participantNotFreeToMakeReservation(UUID sessionId, UUID participantId) {
        return "Participant " + participantId + " not free to make a reservation on session " + sessionId + ".";
    }

    public static final String NOT_FOUND = "Session not found.";

    public static String notFound(UUID sessionId) {
        return "Session with id " + sessionId + " not found.";
    }

    public static final String NOT_FOUND_FOR_RESERVATION = "Session not found to make reservation.";

    public static String notFoundForReservation(UUID sessionId) {
        return "Session with id " + sessionId + " not found to make reservation.";
    }

    public static final String NOT_FOUND_ON_ROOM = "Session not found on room given.";

    public static String notFoundOnRoom(UUID sessionId, UUID roomId) {
        return "Session with id " + sessionId + " not found on room with id " + roomId + ".";
    }

    public static final String ROOM_NOT_FOUND = "Room not found for this session.";

    public static String roomNotFound(UUID roomId, UUID sessionId) {
        return "Failed to get room with id " + roomId + " while retrieving session " + sessionId + ".";
    }
}
