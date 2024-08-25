package com.faroc.gymanager.gymmanagement.application.gyms.commands.addgym;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.common.application.security.authorization.Authorize;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;

import java.util.UUID;

@Authorize(permissions = {"gyms:create"})
public record AddGymCommand(String name, UUID subscriptionId) implements Command<Gym> {
}
