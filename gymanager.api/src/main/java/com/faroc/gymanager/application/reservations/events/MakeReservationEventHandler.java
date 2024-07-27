package com.faroc.gymanager.application.reservations.events;

import com.faroc.gymanager.application.reservations.gateways.ReservationsGateway;
import com.faroc.gymanager.domain.sessions.events.MakeReservationEvent;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MakeReservationEventHandler {
    private final ReservationsGateway reservationsGateway;

    @Autowired

    public MakeReservationEventHandler(ReservationsGateway reservationsGateway) {
        this.reservationsGateway = reservationsGateway;
    }

    @Async
    @TransactionalEventListener
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
