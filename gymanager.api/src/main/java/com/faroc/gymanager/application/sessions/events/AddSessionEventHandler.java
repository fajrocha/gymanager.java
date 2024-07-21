package com.faroc.gymanager.application.sessions.events;

import com.faroc.gymanager.application.sessions.gateways.SessionsGateway;
import com.faroc.gymanager.domain.rooms.events.SessionReservationEvent;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AddSessionEventHandler {

    private final SessionsGateway sessionsGateway;

    @Autowired
    public AddSessionEventHandler(SessionsGateway sessionsGateway) {
        this.sessionsGateway = sessionsGateway;
    }

    @Async
    @TransactionalEventListener
    public void handle(SessionReservationEvent event) {
        var session = event.session();

        try {
            sessionsGateway.create(session);
        } catch (Exception ex) {
            throw new EventualConsistencyException(SessionReservationEvent.sessionAddFailed(session.getId()));
        }
    }
}
