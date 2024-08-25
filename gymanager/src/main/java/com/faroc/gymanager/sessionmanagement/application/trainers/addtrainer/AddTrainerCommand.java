package com.faroc.gymanager.sessionmanagement.application.trainers.addtrainer;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record AddTrainerCommand(UUID userId) implements Command<UUID> {
}
