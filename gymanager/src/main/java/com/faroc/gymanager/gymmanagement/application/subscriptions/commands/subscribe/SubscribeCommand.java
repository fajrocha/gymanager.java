package com.faroc.gymanager.gymmanagement.application.subscriptions.commands.subscribe;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType;

import java.util.UUID;

public record SubscribeCommand(SubscriptionType subscriptionType, UUID adminId)
        implements Command<Subscription> {
}
