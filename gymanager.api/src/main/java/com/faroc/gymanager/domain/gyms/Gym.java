package com.faroc.gymanager.domain.gyms;

import com.faroc.gymanager.domain.gyms.errors.GymsErrors;
import com.faroc.gymanager.domain.gyms.exceptions.MaxRoomsReachedException;
import com.faroc.gymanager.domain.rooms.Room;
import com.faroc.gymanager.domain.rooms.exceptions.MaxSessionsReachedException;
import com.faroc.gymanager.domain.shared.AggregateRoot;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.trainers.Trainer;
import lombok.Getter;

import javax.swing.plaf.SeparatorUI;
import java.util.*;

public class Gym extends AggregateRoot {
    @Getter
    private final UUID subscriptionId;
    @Getter
    private final String name;
    @Getter
    private final int maxRooms;
    private final Set<UUID> roomIds = new HashSet<>();
    private final Set<UUID> trainerIds = new HashSet<>();

    public Gym(UUID subscriptionId, String name, int maxRooms) {
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.maxRooms = maxRooms;
    }

    public Gym(UUID id, UUID subscriptionId, String name, int maxRooms) {
        super(id);
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.maxRooms = maxRooms;
    }

    public void addRoom(Room room) {
        var roomId = room.getId();

        addRoom(roomId);
    }

    public void addRoom(UUID roomId) {
        if (roomIds.contains(roomId))
            throw new ConflictException(
                    GymsErrors.CONFLICT_ROOM,
                    GymsErrors.conflictRoom(roomId, id)
            );

        if (roomIds.size() >= maxRooms)
            throw new MaxRoomsReachedException(GymsErrors.maxRoomsReached(roomId, id));

        roomIds.add(roomId);
    }

    public void addTrainer(Trainer trainer) {
        var trainerId = trainer.getId();

        addRoom(trainerId);
    }

    public void addTrainer(UUID trainerId) {
        if (trainerIds.contains(trainerId))
            throw new ConflictException(
                    GymsErrors.CONFLICT_TRAINER,
                    GymsErrors.conflictTrainer(trainerId, id)
            );

        trainerIds.add(trainerId);
    }

    public Set<UUID> getRoomIds() {
        return Collections.unmodifiableSet(roomIds);
    }

    public Set<UUID> getTrainerIds() {
        return Collections.unmodifiableSet(trainerIds);
    }
}
