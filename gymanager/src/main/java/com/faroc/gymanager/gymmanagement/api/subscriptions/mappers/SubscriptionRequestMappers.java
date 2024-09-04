package com.faroc.gymanager.gymmanagement.api.subscriptions.mappers;

import com.faroc.gymanager.gymmanagement.application.subscriptions.commands.createsubscription.SubscribeCommand;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType;
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.requests.SubscribeRequest;
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi;

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
