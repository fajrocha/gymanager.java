package com.faroc.gymanager.infrastructure.reservations.gateways;

import com.faroc.gymanager.application.reservations.gateways.ReservationsGateway;
import com.faroc.gymanager.domain.sessions.SessionReservation;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.tables.records.SessionReservationsRecord;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.Tables.SESSION_RESERVATIONS;

@Repository
public class ReservationsRepository implements ReservationsGateway {
    private final DSLContext context;

    public ReservationsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void create(SessionReservation reservation, UUID sessionId) {
        var record = new SessionReservationsRecord();

        record.setId(reservation.getId());
        record.setSessionId(sessionId);
        record.setParticipantId(reservation.getParticipantId());

        context.insertInto(SESSION_RESERVATIONS).set(record).execute();
    }
}
