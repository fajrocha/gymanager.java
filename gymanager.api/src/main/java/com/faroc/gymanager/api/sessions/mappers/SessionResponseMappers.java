package com.faroc.gymanager.api.sessions.mappers;

import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.sessions.responses.SessionResponse;

public class SessionResponseMappers {
    public static SessionResponse toResponse(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getName(),
                session.getDescription(),
                session.getCategory(),
                session.getMaximumNumberParticipants(),
                session.getTimeSlot().getStartTime(),
                session.getTimeSlot().getEndTime(),
                session.getTrainerId()
        );
    }
}
