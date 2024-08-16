package com.faroc.gymanager.domain.sessions.events;

import com.faroc.gymanager.domain.sessions.SessionReservation;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.events.DomainEvent;

import java.util.UUID;

public record MakeReservationEvent(Session session, SessionReservation reservation) implements DomainEvent {
    public static String participantNotFound(UUID participantId, UUID reservationId, UUID sessionId) {
        return "Participant " + participantId + " not found to make reservation " + reservationId + " " +
                "on session " + sessionId + ".";
    }
}
