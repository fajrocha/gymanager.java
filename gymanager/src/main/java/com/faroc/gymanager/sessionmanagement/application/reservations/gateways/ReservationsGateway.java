package com.faroc.gymanager.sessionmanagement.application.reservations.gateways;

import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation;

import java.util.UUID;

public interface ReservationsGateway {
    void create(SessionReservation reservation, UUID sessionId);
}
