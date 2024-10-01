package com.faroc.gymanager.sessionmanagement.api.sessions.mappers;

import com.faroc.gymanager.sessionmanagement.api.sessionreservations.contracts.v1.responses.SessionReservationResponse;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.responses.SessionResponse;

public class SessionResponseMappers {
    public static SessionResponse toResponse(Session session) {
        var sessionReservations = session.getReservations().stream()
                .map(r -> new SessionReservationResponse(r.getId(), r.getParticipantId()))
                .toList();

        return new SessionResponse(
                session.getId(),
                session.getName(),
                session.getDescription(),
                session.getCategory(),
                session.getMaximumNumberParticipants(),
                session.getTimeSlot().getStartTime(),
                session.getTimeSlot().getEndTime(),
                session.getTrainerId(),
                sessionReservations
        );
    }
}
