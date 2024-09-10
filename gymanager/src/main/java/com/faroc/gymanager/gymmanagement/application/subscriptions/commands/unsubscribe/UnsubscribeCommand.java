package com.faroc.gymanager.gymmanagement.application.subscriptions.commands.unsubscribe;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

import java.util.UUID;

public record UnsubscribeCommand(UUID subscriptionId) implements Command<Voidy> {
}
