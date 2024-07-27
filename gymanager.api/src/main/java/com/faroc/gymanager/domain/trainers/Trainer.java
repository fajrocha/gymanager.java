package com.faroc.gymanager.domain.trainers;

import com.faroc.gymanager.domain.shared.AggregateRoot;
import com.faroc.gymanager.domain.shared.entities.schedules.Schedule;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import com.faroc.gymanager.domain.trainers.errors.TrainerErrors;
import lombok.Getter;

import java.util.*;
import java.util.stream.Stream;

public class Trainer extends AggregateRoot {
    @Getter
    private final UUID userId;
    private final Set<UUID> sessionsIds = new HashSet<>();

    @Getter
    private final Schedule schedule;

    public Trainer(UUID userId) {
        this.userId = userId;
        this.schedule = Schedule.createEmpty();
    }

    public Trainer(UUID id, UUID userId) {
        super(id);
        this.userId = userId;
        this.schedule = Schedule.createEmpty();
    }

    public Trainer(UUID id, UUID userId, Schedule schedule) {
        super(id);
        this.userId = userId;
        this.schedule = schedule;
    }

    public void makeReservation(Session session) {
        var sessionId = session.getId();
        if (sessionsIds.contains(sessionId))
            throw new ConflictException(
                    TrainerErrors.conflictSession(sessionId, id),
                    TrainerErrors.CONFLICT_SESSION);

        schedule.makeReservation(session.getTimeSlot());

        sessionsIds.add(sessionId);
    }

    public boolean hasSessionReservation(Session session) {
        return sessionsIds.contains(session.getId()) && schedule.hasReservation(session.getTimeSlot());
    }

    public boolean isFree(TimeSlot timeSlot) {
        return schedule.canMakeReservation(timeSlot);
    }

    public void mapSessionId(UUID sessionsId) {
        sessionsIds.add(sessionsId);
    }

    public Set<UUID> getSessionsIds() {
        return Collections.unmodifiableSet(sessionsIds);
    }
}
