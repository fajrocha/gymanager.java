package com.faroc.gymanager.sessionmanagement.domain.sessions.events;

import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.common.domain.events.DomainEvent;

import java.util.UUID;

public record MakeReservationEvent(Session session, SessionReservation reservation) implements DomainEvent {
    public static String participantNotFound(UUID participantId, UUID reservationId, UUID sessionId) {
        return "Participant " + participantId + " not found to make reservation " + reservationId + " " +
                "on session " + sessionId + ".";
    }
}
