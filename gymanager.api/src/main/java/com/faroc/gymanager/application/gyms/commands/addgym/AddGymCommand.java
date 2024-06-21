package com.faroc.gymanager.application.gyms.commands.addgym;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.security.authorization.Authorize;
import com.faroc.gymanager.domain.gyms.Gym;

import java.util.UUID;

@Authorize(permissions = {"gyms:creat"})
public record AddGymCommand(String name, UUID subscriptionId) implements Command<Gym> {
}
