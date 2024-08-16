package com.faroc.gymanager.api.sessions.mappers;

import com.faroc.gymanager.application.sessions.commands.addsession.AddSessionCommand;
import com.faroc.gymanager.sessions.requests.AddSessionRequest;

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
