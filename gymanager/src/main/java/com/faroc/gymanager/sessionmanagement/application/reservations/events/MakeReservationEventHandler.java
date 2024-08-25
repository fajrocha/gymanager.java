package com.faroc.gymanager.sessionmanagement.application.reservations.events;

import com.faroc.gymanager.sessionmanagement.application.reservations.gateways.ReservationsGateway;
import com.faroc.gymanager.sessionmanagement.domain.sessions.events.MakeReservationEvent;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class MakeReservationEventHandler {
    private final ReservationsGateway reservationsGateway;

    @Autowired

    public MakeReservationEventHandler(ReservationsGateway reservationsGateway) {
        this.reservationsGateway = reservationsGateway;
    }

    @ApplicationModuleListener
    public void handle(MakeReservationEvent event) {
        var reservation = event.reservation();
        var session = event.session();

        try {
            reservationsGateway.create(reservation, session.getId());
        } catch (Exception ex) {
            throw new EventualConsistencyException(
                    "Failed to create reservation " + reservation.getId() + ".",
                    ex.getCause());
        }
    }
}
