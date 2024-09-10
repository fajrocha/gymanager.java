package com.faroc.gymanager.gymmanagement.application.gyms.commands.deletegym;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.faroc.gymanager.common.application.security.authorization.Authorize;

import java.util.UUID;

@Authorize(permissions = {"gyms:delete"})
public record RemoveGymCommand(UUID gymId, UUID subscriptionId) implements Command<Voidy> {
}
