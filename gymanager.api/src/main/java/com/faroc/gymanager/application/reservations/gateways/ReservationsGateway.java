package com.faroc.gymanager.application.reservations.gateways;

import com.faroc.gymanager.domain.sessions.SessionReservation;

import java.util.UUID;

public interface ReservationsGateway {
    void create(SessionReservation reservation, UUID sessionId);
}
