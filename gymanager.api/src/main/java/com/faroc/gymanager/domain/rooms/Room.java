package com.faroc.gymanager.domain.rooms;

import com.faroc.gymanager.domain.rooms.errors.RoomErrors;
import com.faroc.gymanager.domain.rooms.exceptions.MaxSessionsReachedException;
import com.faroc.gymanager.domain.shared.entities.schedules.Schedule;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.AggregateRoot;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Room extends AggregateRoot {
    private final UUID gymId;
    private final Set<UUID> sessionsIds = new HashSet<>();
    private final int maxDailySessions;
    private final Schedule schedule;

    public Room(UUID gymId, int maxDailySessions) {
        this.gymId = gymId;
        this.maxDailySessions = maxDailySessions;
        this.schedule = Schedule.createEmpty();
    }

    public void makeReservation(Session session) {
        var sessionId = session.getId();
        if (sessionsIds.contains(sessionId))
            throw new ConflictException(
                    RoomErrors.conflictGym(sessionId, id),
                    RoomErrors.CONFLICT_SESSION);

        if (sessionsIds.size() >= maxDailySessions)
            throw new MaxSessionsReachedException(RoomErrors.maxSessionsReached(sessionId, id));

        schedule.makeReservation(session.getTimeSlot());

        sessionsIds.add(sessionId);
    }

    public boolean hasSessionReservation(Session session) {
        return sessionsIds.contains(session.getId()) && schedule.hasReservation(session.getTimeSlot());
    }
}

