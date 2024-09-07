package com.faroc.gymanager.gymmanagement.application.subscriptions.commands.deletesubscription;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

import java.util.UUID;

public record unsubscribeCommand(UUID subscriptionId) implements Command<Voidy> {
}
