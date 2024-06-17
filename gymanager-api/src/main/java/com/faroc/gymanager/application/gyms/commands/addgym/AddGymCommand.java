package com.faroc.gymanager.application.gyms.commands.addgym;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.domain.gyms.Gym;

import java.util.UUID;

public record AddGymCommand(String name, UUID subscriptionId) implements Command<Gym> {
}
