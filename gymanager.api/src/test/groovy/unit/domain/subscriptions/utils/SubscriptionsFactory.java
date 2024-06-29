package unit.domain.subscriptions.utils;

import com.faroc.gymanager.domain.subscriptions.Subscription;
import com.faroc.gymanager.domain.subscriptions.SubscriptionType;

import java.util.UUID;

public class SubscriptionsFactory {
    public static Subscription create() {
        return new Subscription(
            UUID.randomUUID(),
            SubscriptionConstants.SUBSCRIPTION_TYPE_DEFAULT
        );
    }

    public static Subscription create(UUID id) {
        return new Subscription(
                id,
                UUID.randomUUID(),
                SubscriptionConstants.SUBSCRIPTION_TYPE_DEFAULT
        );
    }

    public static Subscription create(SubscriptionType subscriptionType) {
        return new Subscription(
                UUID.randomUUID(),
                subscriptionType
        );
    }
}