package com.faroc.gymanager.gymmanagement.infrastructure.subscriptions.mappers;

import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType;
import org.jooq.codegen.maven.gymanager.tables.records.SubscriptionsRecord;

import java.util.Arrays;

public class SubscriptionMappers {
    public Subscription toDomain(SubscriptionsRecord subscriptionsRecord) {
        var subscription = new Subscription(
                subscriptionsRecord.getId(),
                subscriptionsRecord.getAdminId(),
                SubscriptionType.valueOf(subscriptionsRecord.getSubscriptionType())
        );

        Arrays.stream(subscriptionsRecord.getGymIds()).forEach(subscription::addGym);

        return subscription;
    }
}
