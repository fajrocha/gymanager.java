package com.faroc.gymanager.sessionmanagement.api.sessions.mappers;

import com.faroc.gymanager.sessionmanagement.application.sessions.commands.addsession.AddSessionCommand;
import com.faroc.gymanager.sessionmanagement.sessions.requests.AddSessionRequest;

import java.util.UUID;

public class SessionRequestMappers {
    public static AddSessionCommand toCommand(AddSessionRequest addSessionRequest, UUID roomId) {
        return new AddSessionCommand(
                addSessionRequest.name(),
                addSessionRequest.description(),
                addSessionRequest.category(),
                addSessionRequest.maxParticipants(),
                addSessionRequest.startTime(),
                addSessionRequest.endTime(),
                addSessionRequest.trainerId(),
                roomId
        );
    }
}
