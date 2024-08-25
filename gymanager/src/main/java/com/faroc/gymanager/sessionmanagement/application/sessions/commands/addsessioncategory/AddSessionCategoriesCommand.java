package com.faroc.gymanager.sessionmanagement.application.sessions.commands.addsessioncategory;

import an.awesome.pipelinr.Command;

import java.util.List;
import java.util.UUID;

public record AddSessionCategoriesCommand(UUID gymId, List<String> sessionCategories) implements Command<List<String>> {
}
