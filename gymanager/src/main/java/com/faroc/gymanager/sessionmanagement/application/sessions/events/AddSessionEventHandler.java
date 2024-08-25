package com.faroc.gymanager.sessionmanagement.application.sessions.events;

import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionsGateway;
import com.faroc.gymanager.sessionmanagement.domain.rooms.events.SessionReservationEvent;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class AddSessionEventHandler {

    private final SessionsGateway sessionsGateway;

    @Autowired
    public AddSessionEventHandler(SessionsGateway sessionsGateway) {
        this.sessionsGateway = sessionsGateway;
    }

    @ApplicationModuleListener
    public void handle(SessionReservationEvent event) {
        var session = event.session();

        try {
            sessionsGateway.create(session);
        } catch (Exception ex) {
            throw new EventualConsistencyException(
                    SessionReservationEvent.sessionAddFailed(session.getId()),
                    ex.getCause());
        }
    }
}
