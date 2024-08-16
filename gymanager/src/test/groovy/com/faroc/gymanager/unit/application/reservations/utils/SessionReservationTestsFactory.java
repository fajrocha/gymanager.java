package com.faroc.gymanager.unit.application.reservations.utils;

import com.faroc.gymanager.domain.sessions.SessionReservation;

import java.util.UUID;

public class SessionReservationTestsFactory {
    public static SessionReservation create() {
        return new SessionReservation(UUID.randomUUID(), UUID.randomUUID());
    }
}
