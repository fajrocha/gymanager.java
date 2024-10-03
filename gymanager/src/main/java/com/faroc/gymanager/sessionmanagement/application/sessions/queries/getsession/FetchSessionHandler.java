package com.faroc.gymanager.sessionmanagement.application.sessions.queries.getsession;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.sessionmanagement.application.rooms.gateways.RoomsGateway;
import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionsGateway;
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.sessionmanagement.domain.sessions.errors.SessionErrors;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
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
                        SessionErrors.fetchSessionRoomNotFound(roomId, sessionId),
                        SessionErrors.FETCH_SESSION_ROOM_NOT_FOUND
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
