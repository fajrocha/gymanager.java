package unit.application.subscriptions.utils;

import com.faroc.gymanager.domain.subscriptions.Subscription;
import com.faroc.gymanager.domain.subscriptions.SubscriptionType;

import java.util.UUID;

public class SubscriptionsTestsFactory {
    public final static SubscriptionType DEFAULT_SUBSCRIPTION_TYPE = SubscriptionType.Free;

    public static Subscription create(UUID id) {
        return new Subscription(
                id,
                UUID.randomUUID(),
                DEFAULT_SUBSCRIPTION_TYPE
        );
    }
}
