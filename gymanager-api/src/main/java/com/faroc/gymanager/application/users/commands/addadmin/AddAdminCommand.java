package com.faroc.gymanager.application.users.commands.addadmin;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record AddAdminCommand(UUID userId) implements Command<UUID> {
}
