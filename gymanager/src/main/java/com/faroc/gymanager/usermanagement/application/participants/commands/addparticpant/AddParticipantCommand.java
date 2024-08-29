package com.faroc.gymanager.usermanagement.application.participants.commands.addparticpant;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record AddParticipantCommand(UUID userId) implements Command<UUID> {
}
