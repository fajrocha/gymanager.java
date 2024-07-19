package com.faroc.gymanager.api.sessions.controllers;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.api.sessions.mappers.SessionRequestMappers;
import com.faroc.gymanager.application.sessions.commands.addsession.AddSessionCommand;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import com.faroc.gymanager.sessions.requests.AddSessionRequest;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/rooms/{roomId}/sessions")
public class SessionsController {

    private final Pipeline pipeline;

    public SessionsController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @GetMapping
    public AddSessionRequest addSession(@PathVariable UUID roomId, @RequestBody AddSessionRequest addSessionRequest) {
        var command = SessionRequestMappers.toCommand(addSessionRequest, roomId);

        command.execute(pipeline);

        return addSessionRequest;
    }
}
