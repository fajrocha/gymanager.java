package com.faroc.gymanager.infrastructure.sessions.gateways;

import com.faroc.gymanager.application.sessions.gateways.SessionsGateway;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.infrastructure.sessions.mappers.SessionPersistenceMappers;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.Tables;
import org.springframework.stereotype.Repository;

@Repository
public class SessionsRepository implements SessionsGateway {
    private final DSLContext context;

    public SessionsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void create(Session session) {
        var sessionRecord = SessionPersistenceMappers.toRecord(session);

        context.insertInto(Tables.SESSIONS).set(sessionRecord).execute();
    }
}
