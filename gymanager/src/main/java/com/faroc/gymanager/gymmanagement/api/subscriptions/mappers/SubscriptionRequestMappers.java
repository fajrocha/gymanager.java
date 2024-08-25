package com.faroc.gymanager.gymmanagement.api.subscriptions.mappers;

import com.faroc.gymanager.gymmanagement.application.subscriptions.commands.createsubscription.SubscribeCommand;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType;
import com.faroc.gymanager.subscriptions.requests.SubscribeRequest;
import com.faroc.gymanager.subscriptions.shared.SubscriptionTypeApi;

public class SubscriptionRequestMappers {
    public static SubscribeCommand toCommand(SubscribeRequest request) {
        return new SubscribeCommand(
                toDomain(request.subscriptionType()),
                request.adminId()
        );
    }

    public static SubscriptionType toDomain(SubscriptionTypeApi subscriptionTypeApi) {
        return switch (subscriptionTypeApi) {
            case Free -> SubscriptionType.Free;
            case Starter -> SubscriptionType.Starter;
            case Pro -> SubscriptionType.Pro;
        };
    }
}
