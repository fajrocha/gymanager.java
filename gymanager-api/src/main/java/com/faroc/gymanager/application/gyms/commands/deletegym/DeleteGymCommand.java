package com.faroc.gymanager.application.gyms.commands.deletegym;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

import java.util.UUID;

public record DeleteGymCommand(UUID gymId, UUID subscriptionId) implements Command<Voidy> {
}
