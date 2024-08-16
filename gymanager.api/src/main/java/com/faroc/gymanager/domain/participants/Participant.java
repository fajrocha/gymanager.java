package com.faroc.gymanager.domain.participants;

import com.faroc.gymanager.domain.participants.errors.ParticipantErrors;
import com.faroc.gymanager.domain.shared.entities.schedules.Schedule;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.AggregateRoot;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Participant extends AggregateRoot {
    @Getter
    private final UUID userId;
    private final Set<UUID> sessionsIds = new HashSet<>();
    @Getter
    private final Schedule schedule;

    public Participant(UUID userId) {
        this.userId = userId;
        this.schedule = Schedule.createEmpty();
    }

    public Participant(UUID id, UUID userId) {
        super(id);
        this.userId = userId;
        this.schedule = Schedule.createEmpty();
    }

    public Participant(UUID id, UUID userId, Schedule schedule) {
        super(id);
        this.userId = userId;
        this.schedule = schedule;
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

    public boolean hasReservation(Session session) {
        return sessionsIds.contains(session.getId()) && schedule.hasReservation(session.getTimeSlot());
    }

    public void cancelReservation(Session session) {
        var sessionId = session.getId();
        if (!sessionsIds.contains(sessionId))
            throw new UnexpectedException(
                    ParticipantErrors.sessionNotFound(sessionId, id),
                    ParticipantErrors.SESSION_NOT_FOUND);

        schedule.makeReservation(session.getTimeSlot());

        sessionsIds.add(sessionId);
    }

    public boolean isFree(TimeSlot timeSlot) {
        return schedule.canMakeReservation(timeSlot);
    }

    public Set<UUID> getSessionsIds() {
        return Collections.unmodifiableSet(sessionsIds);
    }

    public void mapSessionId(UUID sessionsId) {
        sessionsIds.add(sessionsId);
    }
}
