package com.faroc.gymanager.api.subscriptions.mappers;

import com.faroc.gymanager.application.subscriptions.commands.createsubscription.CreateSubscriptionCommand;
import com.faroc.gymanager.domain.subscriptions.SubscriptionType;
import com.faroc.gymanager.subscriptions.requests.CreateSubscriptionRequest;
import com.faroc.gymanager.subscriptions.shared.SubscriptionTypeApi;

public class SubscriptionRequestMappers {
    public static CreateSubscriptionCommand toCommand(CreateSubscriptionRequest request) {
        return new CreateSubscriptionCommand(
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
