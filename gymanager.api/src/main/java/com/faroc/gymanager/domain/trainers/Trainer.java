package com.faroc.gymanager.domain.trainers;

import com.faroc.gymanager.domain.shared.AggregateRoot;
import com.faroc.gymanager.domain.shared.entities.schedules.Schedule;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.trainers.errors.TrainerErrors;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Trainer extends AggregateRoot {
    private final UUID userId;
    private final Set<UUID> sessionsIds = new HashSet<>();
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
}
