package com.faroc.gymanager.application.sessions.commands.addsession;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.rooms.gateways.RoomsGateway;
import com.faroc.gymanager.application.users.gateways.TrainersGateway;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.sessions.errors.SessionErrors;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import org.springframework.stereotype.Component;

@Component
public class AddSessionHandler implements Command.Handler<AddSessionCommand, Session> {

    private final RoomsGateway roomsGateway;
    private final TrainersGateway trainersGateway;

    public AddSessionHandler(RoomsGateway roomsGateway, TrainersGateway trainersGateway) {
        this.roomsGateway = roomsGateway;
        this.trainersGateway = trainersGateway;
    }

    @Override
    public Session handle(AddSessionCommand addSessionCommand) {
        var roomId = addSessionCommand.roomId();

        var room = roomsGateway.findById(roomId)
                .orElseThrow(() -> new UnexpectedException(
                        SessionErrors.roomNotFound(roomId),
                        SessionErrors.ROOM_NOT_FOUND
                ));

        return null;
    }
}
