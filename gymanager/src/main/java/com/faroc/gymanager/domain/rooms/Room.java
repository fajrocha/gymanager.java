package com.faroc.gymanager.domain.rooms;

import com.faroc.gymanager.domain.rooms.errors.RoomErrors;
import com.faroc.gymanager.domain.rooms.events.SessionReservationEvent;
import com.faroc.gymanager.domain.rooms.exceptions.MaxSessionsReachedException;
import com.faroc.gymanager.domain.shared.entities.schedules.Schedule;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.AggregateRoot;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

public class Room extends AggregateRoot {
    @Getter
    private final UUID gymId;
    private final HashMap<LocalDate, Set<UUID>> sessionsIds = new HashMap<>();

    @Getter
    private final int maxDailySessions;

    @Getter
    private final Schedule schedule;

    @Getter
    private final String name;

    public Room(UUID gymId, String name, int maxDailySessions) {
        this.gymId = gymId;
        this.maxDailySessions = maxDailySessions;
        this.name = name;
        this.schedule = Schedule.createEmpty();
    }

    public Room(
            UUID id,
            UUID gymId,
            String name,
            int maxDailySessions) {
        super(id);
        this.gymId = gymId;
        this.maxDailySessions = maxDailySessions;
        this.name = name;
        this.schedule = Schedule.createEmpty();
    }

    public Room(
            UUID id,
            UUID gymId,
            String name,
            int maxDailySessions,
            Schedule schedule) {
        super(id);
        this.gymId = gymId;
        this.maxDailySessions = maxDailySessions;
        this.name = name;
        this.schedule = schedule;
    }

    public void makeReservation(Session session) {
        var sessionDate = session.getDate();
        var sessionId = session.getId();

        if (!sessionsIds.containsKey(sessionDate))
            sessionsIds.put(sessionDate, new HashSet<>());

        var sessionIdsOnDate = sessionsIds.get(sessionDate);

        if (sessionIdsOnDate.contains(sessionId))
            throw new ConflictException(
                    RoomErrors.conflictGym(sessionId, id),
                    RoomErrors.CONFLICT_SESSION);

        if (sessionIdsOnDate.size() >= maxDailySessions)
            throw new MaxSessionsReachedException(RoomErrors.maxSessionsReached(sessionId, id));

        schedule.makeReservation(session.getTimeSlot());

        domainEvents.add(new SessionReservationEvent(this, session));

        sessionIdsOnDate.add(sessionId);
    }

    public boolean hasSessionReservation(Session session) {
        if (sessionsIds.isEmpty())
            return false;

        var sessionIdsOnDate = sessionsIds.get(session.getDate());

        return sessionIdsOnDate.contains(session.getId()) && schedule.hasReservation(session.getTimeSlot());
    }

    public Map<LocalDate, Set<UUID>> getSessionsIds(){
        return Collections.unmodifiableMap(sessionsIds);
    }

    public void mapSessionIdsFrom(Map<LocalDate, Set<UUID>> sessionIds) {
        sessionsIds.putAll(sessionIds);
    }
}

