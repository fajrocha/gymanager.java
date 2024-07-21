package com.faroc.gymanager.infrastructure.gyms.gateways;

import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.domain.gyms.Gym;
import com.faroc.gymanager.infrastructure.gyms.mappers.GymMappers;
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
        context.update(GYMS)
                .set(GYMS.SUBSCRIPTION_ID, gym.getSubscriptionId())
                .set(GYMS.NAME, gym.getName())
                .set(GYMS.MAX_ROOMS, gym.getMaxRooms())
                .set(GYMS.ROOM_IDS, gym.getRoomIds().toArray(new UUID[0]))
                .set(GYMS.TRAINER_IDS, gym.getTrainerIds().toArray(new UUID[0]))
                .set(GYMS.SESSION_CATEGORIES, gym.getSessionCategories().toArray(new String[0]))
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
