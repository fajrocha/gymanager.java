package com.faroc.gymanager.domain.gyms;

import com.faroc.gymanager.domain.shared.AggregateRoot;
import lombok.Getter;

import javax.swing.plaf.SeparatorUI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Gym extends AggregateRoot {
    private final UUID subscriptionId;
    private final String name;
    private final int maxRooms;
    private final Set<UUID> roomIds = new HashSet<>();
    private final Set<UUID> trainerIds = new HashSet<>();

    public Gym(UUID subscriptionId, String name) {
        this.subscriptionId = subscriptionId;
        this.name = name;
        maxRooms = Integer.MAX_VALUE;
    }

    private Gym(UUID id, UUID subscriptionId, String name, int maxRooms) {
        super(id);
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.maxRooms = maxRooms;
    }

    public static Gym mapFromStorage(
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
