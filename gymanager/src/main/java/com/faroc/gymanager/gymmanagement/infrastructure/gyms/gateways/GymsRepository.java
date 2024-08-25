package com.faroc.gymanager.gymmanagement.infrastructure.gyms.gateways;

import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import com.faroc.gymanager.gymmanagement.infrastructure.gyms.mappers.GymMappers;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.jooq.codegen.maven.gymanager.Tables.GYMS;

@Repository
public class GymsRepository implements GymsGateway {
    private final DSLContext context;

    @Autowired
    public GymsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void save(Gym gym) {
        var gymRecord = GymMappers.toRecord(gym);

        context.insertInto(GYMS).set(gymRecord).execute();
    }

    @Override
    public void update(Gym gym) {
        var gymRecord = GymMappers.toRecordUpdate(gym);

        context.update(GYMS)
                .set(gymRecord)
                .where(GYMS.ID.eq(gym.getId()))
                .execute();
    }

    @Override
    public Optional<Gym> findById(UUID id) {
        var gymRecord = context.selectFrom(GYMS).where(GYMS.ID.eq(id)).fetchOne();

        if (gymRecord == null)
            return Optional.empty();

        var gym = GymMappers.toDomain(gymRecord);

        return Optional.of(gym);
    }

    @Override
    public List<Gym> findBySubscriptionId(UUID subscriptionId) {
        var gymsRecords = context.selectFrom(GYMS).where(GYMS.SUBSCRIPTION_ID.eq(subscriptionId)).fetchArray();

        return Arrays.stream(gymsRecords).map(GymMappers::toDomain).toList();
    }

    @Override
    public void delete(Gym gym) {
        context.delete(GYMS).where(GYMS.ID.eq(gym.getId())).execute();
    }

    @Override
    public void deleteBySubscription(UUID subscriptionId) {
        context.delete(GYMS).where(GYMS.SUBSCRIPTION_ID.eq(subscriptionId)).execute();
    }
}
