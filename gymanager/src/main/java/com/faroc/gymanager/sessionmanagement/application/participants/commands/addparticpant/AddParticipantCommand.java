package com.faroc.gymanager.sessionmanagement.application.participants.commands.addparticpant;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record AddParticipantCommand(UUID userId) implements Command<UUID> {
}
