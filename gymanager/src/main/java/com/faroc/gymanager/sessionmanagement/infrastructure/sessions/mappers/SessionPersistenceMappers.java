package com.faroc.gymanager.sessionmanagement.infrastructure.sessions.mappers;

import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.sessionmanagement.domain.common.time.TimeUtils;
import org.jooq.codegen.maven.gymanager.tables.records.SessionsRecord;

public class SessionPersistenceMappers {
    public static SessionsRecord toRecord(Session session) {
        var sessionRecord = new SessionsRecord();

        return sessionRecord;
    }
}
