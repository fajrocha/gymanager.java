package com.faroc.gymanager.gymmanagement.application.gyms.commands.addsessioncategory;

import an.awesome.pipelinr.Command;

import java.util.List;
import java.util.UUID;

public record AddSessionCategoriesCommand(
        UUID gymId,
        UUID subscriptionId,
        List<String> sessionCategories) implements Command<List<String>> {
}
