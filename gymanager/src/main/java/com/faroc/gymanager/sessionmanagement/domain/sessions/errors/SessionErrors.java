package com.faroc.gymanager.sessionmanagement.domain.sessions.errors;

import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot;

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

    public static final String FETCH_SESSION_ROOM_NOT_FOUND = "Room not found for this session.";

    public static String fetchSessionRoomNotFound(UUID roomId, UUID sessionId) {
        return "Failed to get room with id " + roomId + " while fetching session " + sessionId + ".";
    }

    public static String ADD_SESSION_ROOM_NOT_FOUND = "Room was not found.";
    public static String addSessionRoomNotFound(UUID roomId) {
        return "Failed to add session. Room with id " + roomId + " not found.";
    }

    public static String TRAINER_NOT_FOUND = "Trainer was not found.";
    public static String trainerNotFound(UUID trainerId) {
        return "Failed to add session. Trainer with id " + trainerId + " not found.";
    }

    public static String SESSION_CATEGORY_NOT_SUPPORTED = "Session category not supported.";
    public static String sessionCategoryNotSupported() {
        return "Failed to add session. Session category not supported.";
    }

    public static String TRAINER_SCHEDULE_CONFLICT = "Trainer is not available during the time range selected.";
    public static String trainerScheduleConflict(UUID trainerId, TimeSlot timeSlot) {
        return "Failed to add session. Trainer with id " + trainerId + " is already busy during " +
                "the time selected between " + timeSlot.getStartTime()  + " and " + timeSlot.getEndTime() + ".";
    }
}
