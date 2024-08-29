package com.faroc.gymanager.gymmanagement.infrastructure.gyms.mappers;

import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import org.jooq.codegen.maven.gymanager.tables.records.GymsRecord;

import java.util.Arrays;
import java.util.UUID;

public class GymMappers {
    public static GymsRecord toRecord(Gym gym) {
        var gymRecord = new GymsRecord();

        gymRecord.setId(gym.getId());
        mapNonIdProperties(gymRecord, gym);

        return gymRecord;
    }

    public static GymsRecord toRecordUpdate(Gym gym) {
        var gymRecord = new GymsRecord();
        mapNonIdProperties(gymRecord, gym);

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

    private static void mapNonIdProperties(GymsRecord gymRecord, Gym gym) {
        gymRecord.setSubscriptionId(gym.getSubscriptionId());
        gymRecord.setName(gym.getName());
        gymRecord.setMaxRooms(gym.getMaxRooms());
        gymRecord.setRoomIds(gym.getRoomIds().toArray(new UUID[0]));
        gymRecord.setTrainerIds(gym.getTrainerIds().toArray(new UUID[0]));
        gymRecord.setSessionCategories(gym.getSessionCategories().toArray(new String[0]));
    }
}
