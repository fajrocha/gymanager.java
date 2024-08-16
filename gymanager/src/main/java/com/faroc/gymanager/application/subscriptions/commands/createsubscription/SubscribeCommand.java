package com.faroc.gymanager.application.subscriptions.commands.createsubscription;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.domain.subscriptions.Subscription;
import com.faroc.gymanager.domain.subscriptions.SubscriptionType;

import java.util.UUID;

public record SubscribeCommand(SubscriptionType subscriptionType, UUID adminId)
        implements Command<Subscription> {
}
