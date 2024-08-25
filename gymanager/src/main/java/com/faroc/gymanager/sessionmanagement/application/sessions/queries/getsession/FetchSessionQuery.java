package com.faroc.gymanager.sessionmanagement.application.sessions.queries.getsession;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;

import java.util.UUID;

public record FetchSessionQuery(UUID roomId, UUID sessionId) implements Command<Session> {
}
