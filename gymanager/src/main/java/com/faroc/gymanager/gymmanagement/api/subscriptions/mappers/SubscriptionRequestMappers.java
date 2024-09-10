package com.faroc.gymanager.gymmanagement.api.subscriptions.mappers;

import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.requests.SubscribeRequest;
import com.faroc.gymanager.gymmanagement.application.subscriptions.commands.subscribe.SubscribeCommand;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType;

public class SubscriptionRequestMappers {
    public static SubscribeCommand toCommand(SubscribeRequest request) {
        return new SubscribeCommand(
                toDomain(request.subscriptionType()),
                request.adminId()
        );
    }

    public static SubscriptionType toDomain(String subscriptionTypeApi) {
        return switch (subscriptionTypeApi.toLowerCase()) {
            case "free" -> SubscriptionType.Free;
            case "starter" -> SubscriptionType.Starter;
            case "pro" -> SubscriptionType.Pro;
            default -> throw new IllegalStateException("Unexpected value: " + subscriptionTypeApi.toLowerCase());
        };
    }
}
