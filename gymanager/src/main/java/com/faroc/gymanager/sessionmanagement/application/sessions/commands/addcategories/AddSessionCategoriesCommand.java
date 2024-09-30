package com.faroc.gymanager.sessionmanagement.application.sessions.commands.addcategories;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory;

import java.util.List;

public record AddSessionCategoriesCommand(List<String> sessionCategories) implements Command<List<SessionCategory>> {
}
