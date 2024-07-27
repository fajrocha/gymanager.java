package com.faroc.gymanager.infrastructure.sessions.mappers;

import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.time.TimeUtils;
import org.jooq.codegen.maven.gymanager.tables.records.SessionsRecord;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class SessionPersistenceMappers {
    public static SessionsRecord toRecord(Session session) {
        var sessionRecord = new SessionsRecord();

        sessionRecord.setId(session.getId());
        sessionRecord.setSessionCategory(session.getCategory());
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

        return sessionRecord;
    }
}
