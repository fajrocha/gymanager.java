package com.faroc.gymanager.sessionmanagement.api.sessions.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.sessionmanagement.api.sessions.mappers.SessionRequestMappers;
import com.faroc.gymanager.sessionmanagement.api.sessions.mappers.SessionResponseMappers;
import com.faroc.gymanager.sessionmanagement.application.sessions.queries.getsession.FetchSessionQuery;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.requests.AddSessionRequest;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.responses.SessionResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("Sessions Controller V1")
@RequestMapping("v1/rooms/{roomId}/sessions")
@Tag(name = "Sessions")
public class SessionsController {

    private final Pipeline pipeline;

    public SessionsController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public SessionResponse addSession(@PathVariable UUID roomId, @RequestBody AddSessionRequest addSessionRequest) {
        var command = SessionRequestMappers.toCommand(addSessionRequest, roomId);

        var session = command.execute(pipeline);

        return SessionResponseMappers.toResponse(session);
    }

    @GetMapping("{sessionId}")
    public SessionResponse fetchSession(@PathVariable UUID roomId, @PathVariable UUID sessionId) {
        var query = new FetchSessionQuery(roomId, sessionId);

        var session = query.execute(pipeline);

        return SessionResponseMappers.toResponse(session);
    }
}
