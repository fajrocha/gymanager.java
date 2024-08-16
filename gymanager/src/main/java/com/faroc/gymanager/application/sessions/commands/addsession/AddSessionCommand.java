package com.faroc.gymanager.application.sessions.commands.addsession;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.domain.sessions.Session;

import java.time.Instant;
import java.util.UUID;

public record AddSessionCommand(
        String name,
        String description,
        String category,
        int maxParticipants,
        Instant startTime,
        Instant endTime,
        UUID trainerId,
        UUID roomId
) implements Command<Session> {
}
