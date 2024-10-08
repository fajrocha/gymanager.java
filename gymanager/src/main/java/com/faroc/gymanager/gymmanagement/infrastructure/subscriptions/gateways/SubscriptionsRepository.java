package com.faroc.gymanager.gymmanagement.infrastructure.subscriptions.gateways;

import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;
import com.faroc.gymanager.gymmanagement.infrastructure.subscriptions.mappers.SubscriptionMappers;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.tables.records.SubscriptionsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
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

    @Override
    public void update(Subscription subscription) {
        context.update(SUBSCRIPTIONS)
                .set(SUBSCRIPTIONS.ADMIN_ID, subscription.getAdminId())
                .set(SUBSCRIPTIONS.SUBSCRIPTION_TYPE, subscription.getSubscriptionType().toString())
                .set(SUBSCRIPTIONS.GYM_IDS, subscription.getGymIds().toArray(new UUID[0]))
                .set(SUBSCRIPTIONS.MAX_GYMS, subscription.getMaxGyms())
                .execute();
    }

    @Override
    public Optional<Subscription> findById(UUID id) {
        var subscriptionRecord = context.selectFrom(SUBSCRIPTIONS).where(SUBSCRIPTIONS.ID.eq(id)).fetchOne();

        if (subscriptionRecord == null)
            return Optional.empty();

        var subscription = SubscriptionMappers.toDomain(subscriptionRecord);

        return Optional.of(subscription);
    }

    @Override
    public void delete(UUID subscriptionId) {
        context.delete(SUBSCRIPTIONS)
                .where(SUBSCRIPTIONS.ID.eq(subscriptionId))
                .execute();
    }

    @Override
    public void delete(Subscription subscription) {
        this.delete(subscription.getId());
    }
}
