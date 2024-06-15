package com.faroc.gymanager.infrastructure.subscriptions.gateways;

import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.domain.subscriptions.Subscription;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.tables.records.SubscriptionsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.Tables.SUBSCRIPTIONS;

@Repository
public class SubscriptionsRepository implements SubscriptionsGateway {
    private final DSLContext context;

    @Autowired
    public SubscriptionsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void save(Subscription subscription) {
        var subscriptionRecord = new SubscriptionsRecord();
        subscriptionRecord.setId(subscription.getId());
        subscriptionRecord.setAdminId(subscription.getAdminId());
        subscriptionRecord.setSubscriptionType(subscription.getSubscriptionType().toString());
        subscriptionRecord.setMaxGyms(subscription.getMaxGyms());
        subscriptionRecord.setGymIds(subscription.getGymIds().toArray(new UUID[0]));

        context.insertInto(SUBSCRIPTIONS).set(subscriptionRecord).execute();
    }
}
