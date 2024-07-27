package com.faroc.gymanager.application.sessions.queries.getsession;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.rooms.gateways.RoomsGateway;
import com.faroc.gymanager.application.sessions.gateways.SessionsGateway;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.sessions.SessionErrors;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import org.springframework.stereotype.Component;

@Component
public class FetchSessionHandler implements Command.Handler<FetchSessionQuery, Session> {
    private final RoomsGateway roomsGateway;
    private final SessionsGateway sessionGateway;

    public FetchSessionHandler(RoomsGateway roomsGateway, SessionsGateway sessionGateway) {
        this.roomsGateway = roomsGateway;
        this.sessionGateway = sessionGateway;
    }

    @Override
    public Session handle(FetchSessionQuery getSessionQuery) {
        var roomId = getSessionQuery.roomId();
        var sessionId = getSessionQuery.sessionId();

        var room = roomsGateway.findById(roomId)
                .orElseThrow(() -> new UnexpectedException(
                        SessionErrors.roomNotFound(roomId, sessionId),
                        SessionErrors.PARTICIPANT_NOT_FOUND
                ));

        var session = sessionGateway.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SessionErrors.notFound(sessionId),
                        SessionErrors.NOT_FOUND
                ));

        if (!room.hasSessionReservation(session))
            throw new UnexpectedException(
                    SessionErrors.notFoundOnRoom(sessionId, roomId),
                    SessionErrors.NOT_FOUND_ON_ROOM);

        return session;
    }
}
