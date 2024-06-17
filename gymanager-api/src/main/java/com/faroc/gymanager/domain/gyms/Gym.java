package com.faroc.gymanager.domain.gyms;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Gym {
    private final UUID id;
    private final UUID subscriptionId;
    private final String name;
    private final int maxRooms;
    private final Set<UUID> roomIds = new HashSet<>();
    private final Set<UUID> trainerIds = new HashSet<>();

    public Gym(UUID subscriptionId, String name) {
        this.id = UUID.randomUUID();
        this.subscriptionId = subscriptionId;
        this.name = name;
        maxRooms = Integer.MAX_VALUE;
    }

    private Gym(UUID id, UUID subscriptionId, String name, int maxRooms) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.maxRooms = maxRooms;
    }

    public static Gym MapFromStorage(
            UUID id,
            UUID subscriptionId,
            String name,
            int maxRooms,
            UUID[] roomIds,
            UUID[] trainerIds) {
        var gym = new Gym(id, subscriptionId, name, maxRooms);
        Arrays.stream(roomIds).forEach(roomId -> gym.getRoomIds().add(roomId));
        Arrays.stream(trainerIds).forEach(trainerId -> gym.getTrainerIds().add(trainerId));

        return gym;
    }
}
