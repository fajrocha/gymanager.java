package com.faroc.gymanager.api.subscriptions.mappers;

import com.faroc.gymanager.domain.subscriptions.Subscription;
import com.faroc.gymanager.domain.subscriptions.SubscriptionType;
import com.faroc.gymanager.subscriptions.responses.SubscriptionResponse;
import com.faroc.gymanager.subscriptions.shared.SubscriptionTypeApi;

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
