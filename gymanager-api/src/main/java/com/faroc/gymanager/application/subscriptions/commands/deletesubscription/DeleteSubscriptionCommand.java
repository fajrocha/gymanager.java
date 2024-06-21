package com.faroc.gymanager.application.subscriptions.commands.deletesubscription;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

import java.util.UUID;

public record DeleteSubscriptionCommand(UUID subscriptionId) implements Command<Voidy> {
}
