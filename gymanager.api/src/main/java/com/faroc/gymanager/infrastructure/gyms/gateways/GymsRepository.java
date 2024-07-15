package com.faroc.gymanager.infrastructure.gyms.gateways;

import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.domain.gyms.Gym;
import com.faroc.gymanager.infrastructure.gyms.mappers.GymMappers;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.tables.records.GymsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
        var gymRecord = GymMappers.toRecord(gym);

        context.update(GYMS).set(gymRecord).execute();
    }

    @Override
    public Optional<Gym> findById(UUID id) {
        var gymRecord = context.selectFrom(GYMS).where(GYMS.ID.eq(id)).fetchOne();

        if (gymRecord == null)
            return Optional.empty();

        var gym = new Gym(
                gymRecord.getId(),
                gymRecord.getSubscriptionId(),
                gymRecord.getName(),
                gymRecord.getMaxRooms()
        );
        Arrays.stream(gymRecord.getRoomIds()).forEach(gym::addRoom);
        Arrays.stream(gymRecord.getTrainerIds()).forEach(gym::addTrainer);

        return Optional.of(gym);
    }

    @Override
    public List<Gym> findBySubscriptionId(UUID subscriptionId) {
        var gymsRecords = context.selectFrom(GYMS).where(GYMS.SUBSCRIPTION_ID.eq(subscriptionId)).fetchArray();
        List<Gym> gyms = new ArrayList<>();

       Arrays.stream(gymsRecords).forEach(gymRecord -> {
            var gym = new Gym(
                    gymRecord.getId(),
                    gymRecord.getSubscriptionId(),
                    gymRecord.getName(),
                    gymRecord.getMaxRooms()
            );

            Arrays.stream(gymRecord.getRoomIds()).forEach(gym::addRoom);
            Arrays.stream(gymRecord.getTrainerIds()).forEach(gym::addTrainer);

           gyms.add(gym);
        });

        return gyms;
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
