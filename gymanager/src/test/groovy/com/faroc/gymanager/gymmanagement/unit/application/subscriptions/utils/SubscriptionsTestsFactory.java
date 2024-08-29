package com.faroc.gymanager.gymmanagement.unit.application.subscriptions.utils;

import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType;

import java.util.List;
import java.util.UUID;

public class SubscriptionsTestsFactory {
    public final static SubscriptionType DEFAULT_SUBSCRIPTION_TYPE = SubscriptionType.Free;

    public static Subscription create() {
        return new Subscription(
                UUID.randomUUID(),
                UUID.randomUUID(),
                DEFAULT_SUBSCRIPTION_TYPE
        );
    }

    public static Subscription create(UUID id) {
        return new Subscription(
                id,
                UUID.randomUUID(),
                DEFAULT_SUBSCRIPTION_TYPE
        );
    }

    public static Subscription create(UUID id, List<UUID> gyms) {
        var subscription = new Subscription(
                id,
                UUID.randomUUID(),
                DEFAULT_SUBSCRIPTION_TYPE
        );

        gyms.forEach(subscription::addGym);

        return subscription;
    }
}
