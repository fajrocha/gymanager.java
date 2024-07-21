package com.faroc.gymanager.infrastructure.gyms.mappers;

import com.faroc.gymanager.domain.gyms.Gym;
import org.jooq.codegen.maven.gymanager.tables.records.GymsRecord;

import java.util.Arrays;
import java.util.UUID;

public class GymMappers {
    public static GymsRecord toRecord(Gym gym) {
        var gymRecord = new GymsRecord();

        gymRecord.setId(gym.getId());
        gymRecord.setSubscriptionId(gym.getSubscriptionId());
        gymRecord.setName(gym.getName());
        gymRecord.setMaxRooms(gym.getMaxRooms());
        gymRecord.setRoomIds(gym.getRoomIds().toArray(new UUID[0]));
        gymRecord.setTrainerIds(gym.getTrainerIds().toArray(new UUID[0]));
        gymRecord.setSessionCategories(gym.getSessionCategories().toArray(new String[0]));

        return gymRecord;
    }

    public static Gym toDomain(GymsRecord gymRecord) {
        var gym = new Gym(
                gymRecord.getId(),
                gymRecord.getSubscriptionId(),
                gymRecord.getName(),
                gymRecord.getMaxRooms()
        );

        Arrays.stream(gymRecord.getRoomIds()).forEach(gym::addRoom);
        Arrays.stream(gymRecord.getTrainerIds()).forEach(gym::addTrainer);
        Arrays.stream(gymRecord.getSessionCategories()).forEach(gym::addCategory);

        return gym;
    }
}
