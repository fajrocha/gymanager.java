package com.faroc.gymanager.application.users.commands.addtrainer;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record AddTrainerCommand(UUID userId) implements Command<UUID> {
}
