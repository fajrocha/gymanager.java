package com.faroc.gymanager.gymmanagement.api.subscriptions.mappers;

import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType;
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.responses.SubscriptionResponse;
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi;

public class SubscriptionResponseMappers {
    public static SubscriptionResponse toResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                toResponse(subscription.getSubscriptionType())
        );
    }

    public static SubscriptionTypeApi toResponse(SubscriptionType subscriptionTypeApi) {
        return switch (subscriptionTypeApi) {
            case Free -> SubscriptionTypeApi.Free;
            case Starter -> SubscriptionTypeApi.Starter;
            case Pro -> SubscriptionTypeApi.Pro;
        };
    }
}
