package com.faroc.gymanager.infrastructure.sessions.gateways;

import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionsGateway;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot;
import com.faroc.gymanager.infrastructure.sessions.mappers.SessionPersistenceMappers;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.Tables.SESSIONS;
import static org.jooq.codegen.maven.gymanager.Tables.SESSION_RESERVATIONS;

@Repository
public class SessionsRepository implements SessionsGateway {
    private final DSLContext context;

    public SessionsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void create(Session session) {
        var sessionRecord = SessionPersistenceMappers.toRecord(session);

        context.insertInto(SESSIONS).set(sessionRecord).execute();
    }

    @Override
    public Optional<Session> findById(UUID id) {
        var fetchResult = context.select()
                .from(SESSIONS)
                .leftJoin(SESSION_RESERVATIONS)
                .on(SESSIONS.ID.eq(SESSION_RESERVATIONS.SESSION_ID))
                .where(SESSIONS.ID.eq(id))
                .fetch();

        if (fetchResult.isEmpty())
            return Optional.empty();

        Session session = null;

        for (var record : fetchResult) {
            if (session == null) {
                var startTime = record.get(SESSIONS.TIME_START);
                var endTime = record.get(SESSIONS.TIME_END);

                var timeSlot = new TimeSlot(
                        startTime.toInstant(),
                        endTime.toInstant()
                );

                session = new Session(
                        record.get(SESSIONS.ID),
                        record.get(SESSIONS.TRAINER_ID),
                        record.get(SESSIONS.ROOM_ID),
                        timeSlot,
                        record.get(SESSIONS.NAME),
                        record.get(SESSIONS.DESCRIPTION),
                        record.get(SESSIONS.SESSION_CATEGORY),
                        record.get(SESSIONS.MAX_PARTICIPANTS)
                );
            }

            var reservationId = record.get(SESSION_RESERVATIONS.ID);

            if (reservationId == null)
                continue;

            var reservation = new SessionReservation(
                    record.get(SESSION_RESERVATIONS.ID),
                    record.get(SESSION_RESERVATIONS.PARTICIPANT_ID)
            );

            session.makeReservation(reservation);
        }

        return Optional.of(session);
    }
}
