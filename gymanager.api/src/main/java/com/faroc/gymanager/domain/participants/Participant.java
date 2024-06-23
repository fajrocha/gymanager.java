package com.faroc.gymanager.domain.participants;

import com.faroc.gymanager.domain.participants.errors.ParticipantErrors;
import com.faroc.gymanager.domain.schedules.Schedule;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.shared.strategicpatterns.Entity;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Participant extends Entity {
    private final UUID userId;
    private final Set<UUID> sessionsIds = new HashSet<>();
    private final Schedule schedule;

    public Participant(UUID userId) {
        super();
        this.schedule = Schedule.createEmpty();
        this.userId = userId;
    }

    public void makeReservation(Session session) {
        var sessionId = session.getId();
        if (sessionsIds.contains(sessionId))
            throw new ConflictException(
                    ParticipantErrors.conflictSession(sessionId, id),
                    ParticipantErrors.CONFLICT_SESSION);

        schedule.makeReservation(session.getTimeSlot());

        sessionsIds.add(sessionId);
    }

    public boolean hasSessionReservation(Session session) {
        return sessionsIds.contains(session.getId()) && schedule.hasReservation(session.getTimeSlot());
    }
}
