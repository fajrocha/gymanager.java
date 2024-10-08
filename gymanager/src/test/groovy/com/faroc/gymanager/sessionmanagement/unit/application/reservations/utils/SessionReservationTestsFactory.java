package com.faroc.gymanager.sessionmanagement.unit.application.reservations.utils;

import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation;

import java.util.UUID;

public class SessionReservationTestsFactory {
    public static SessionReservation create() {
        return new SessionReservation(UUID.randomUUID(), UUID.randomUUID());
    }
}
