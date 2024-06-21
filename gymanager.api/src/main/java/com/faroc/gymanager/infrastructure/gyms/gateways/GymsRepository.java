package com.faroc.gymanager.infrastructure.gyms.gateways;

import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.domain.gyms.Gym;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.tables.records.GymsRecord;
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
        var gymRecord = new GymsRecord();

        gymRecord.setId(gym.getId());
        gymRecord.setSubscriptionId(gym.getSubscriptionId());
        gymRecord.setName(gym.getName());
        gymRecord.setMaxRooms(gym.getMaxRooms());
        gymRecord.setRoomIds(gym.getRoomIds().toArray(new UUID[0]));
        gymRecord.setTrainerIds(gym.getTrainerIds().toArray(new UUID[0]));

        context.insertInto(GYMS).set(gymRecord).execute();
    }

    @Override
    public Optional<Gym> findById(UUID id) {
        var gymRecord = context.selectFrom(GYMS).where(GYMS.ID.eq(id)).fetchOne();

        if (gymRecord == null)
            return Optional.empty();

        var gym = Gym.mapFromStorage(
                gymRecord.getId(),
                gymRecord.getSubscriptionId(),
                gymRecord.getName(),
                gymRecord.getMaxRooms(),
                gymRecord.getRoomIds(),
                gymRecord.getTrainerIds()
        );

        return Optional.of(gym);
    }

    @Override
    public List<Gym> findBySubscriptionId(UUID subscriptionId) {
        var gymsRecords = context.selectFrom(GYMS).where(GYMS.SUBSCRIPTION_ID.eq(subscriptionId)).fetchArray();
        List<Gym> gyms = new ArrayList<>();

        Arrays.stream(gymsRecords).forEach(gymRecord ->
            gyms.add(Gym.mapFromStorage(
                    gymRecord.getId(),
                    gymRecord.getSubscriptionId(),
                    gymRecord.getName(),
                    gymRecord.getMaxRooms(),
                    gymRecord.getRoomIds(),
                    gymRecord.getTrainerIds()
            ))
        );

        return gyms;
    }

    @Override
    public void delete(Gym gym) {
        context.delete(GYMS).where(GYMS.ID.eq(gym.getId())).execute();
    }

    @Override
    public void deleteBySubscriptionId(UUID subscriptionId) {
        context.delete(GYMS).where(GYMS.SUBSCRIPTION_ID.eq(subscriptionId)).execute();
    }
}
