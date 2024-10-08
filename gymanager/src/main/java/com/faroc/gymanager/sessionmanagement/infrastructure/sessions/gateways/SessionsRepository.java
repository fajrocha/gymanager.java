package com.faroc.gymanager.sessionmanagement.infrastructure.sessions.gateways;

import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionsGateway;
import com.faroc.gymanager.sessionmanagement.domain.common.time.TimeUtils;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.tables.records.SessionsRecord;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.Tables.*;

@Repository
public class SessionsRepository implements SessionsGateway {
    private final DSLContext context;

    public SessionsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void create(Session session) {
        var sessionRecord = new SessionsRecord();

        sessionRecord.setId(session.getId());
        sessionRecord.setSessionCategoryId(session.getCategory().getId());
        sessionRecord.setDate(session.getDate());
        var startTime = TimeUtils.toOffsetDateTimeFromInstant(session.getTimeSlot().getStartTime());
        sessionRecord.setTimeStart(startTime);
        var endTime = TimeUtils.toOffsetDateTimeFromInstant(session.getTimeSlot().getEndTime());
        sessionRecord.setTimeEnd(endTime);
        sessionRecord.setName(session.getName());
        sessionRecord.setDescription(session.getDescription());
        sessionRecord.setMaxParticipants(session.getMaximumNumberParticipants());
        sessionRecord.setRoomId(session.getRoomId());
        sessionRecord.setTrainerId(session.getTrainerId());

        context.insertInto(SESSIONS).set(sessionRecord).execute();
    }

    @Override
    public Optional<Session> findById(UUID id) {
        var fetchResult = context.select()
                .from(SESSIONS)
                .join(SESSION_CATEGORIES)
                .on(SESSIONS.SESSION_CATEGORY_ID.eq(SESSION_CATEGORIES.ID))
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
                var sessionCategoryId = record.get(SESSION_CATEGORIES.ID);
                var sessionCategoryName = record.get(SESSION_CATEGORIES.NAME);

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
                        new SessionCategory(sessionCategoryId, sessionCategoryName),
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
