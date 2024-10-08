package com.faroc.gymanager.gymmanagement.domain.gyms;

import com.faroc.gymanager.gymmanagement.domain.gyms.errors.GymsErrors;
import com.faroc.gymanager.gymmanagement.domain.gyms.events.AddRoomEvent;
import com.faroc.gymanager.gymmanagement.domain.gyms.exceptions.MaxRoomsReachedException;
import com.faroc.gymanager.common.domain.AggregateRoot;
import com.faroc.gymanager.common.domain.exceptions.ConflictException;
import com.faroc.gymanager.gymmanagement.domain.rooms.RoomGym;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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
    private final Set<String> sessionCategories = new HashSet<>();

    public Gym(UUID subscriptionId, String name, int maxRooms) {
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.maxRooms = maxRooms;
    }

    public Gym(
            @JsonProperty("id") UUID id,
            @JsonProperty("subscriptionId") UUID subscriptionId,
            @JsonProperty("name") String name,
            @JsonProperty("maxRooms") int maxRooms) {
        super(id);
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.maxRooms = maxRooms;
    }

    public void addRoom(RoomGym room) {
        var roomId = room.getId();

        addRoom(roomId);

        domainEvents.add(new AddRoomEvent(room));
    }

    public void addRoom(UUID roomId) {
        if (roomIds.contains(roomId))
            throw new ConflictException(
                    GymsErrors.conflictRoom(roomId, id),
                    GymsErrors.CONFLICT_ROOM);

        if (roomIds.size() >= maxRooms)
            throw new MaxRoomsReachedException(GymsErrors.maxRoomsReached(roomId, id));

        roomIds.add(roomId);
    }

    public boolean hasRoom(RoomGym room) {
        return hasRoom(room.getId());
    }

    public boolean hasRoom(UUID roomId) {
        return roomIds.contains(roomId);
    }

    public void addCategory(String sessionCategory) {
        if (sessionCategories.contains(sessionCategory))
            throw new ConflictException(
                    GymsErrors.conflictSessionCategory(sessionCategory, id),
                    GymsErrors.conflictSessionCategory(sessionCategory));

        sessionCategories.add(sessionCategory);
    }

    public void addTrainer(UUID trainerId) {
        if (trainerIds.contains(trainerId))
            throw new ConflictException(
                    GymsErrors.conflictTrainer(trainerId, id),
                    GymsErrors.CONFLICT_TRAINER
            );

        trainerIds.add(trainerId);
    }
    
    public boolean hasTrainer(UUID trainerId) {
        return trainerIds.contains(trainerId);
    }
    public boolean hasCategory(String categoryName) {
        return sessionCategories.contains(categoryName);
    }
    public Set<UUID> getRoomIds() {
        return Collections.unmodifiableSet(roomIds);
    }
    public Set<UUID> getTrainerIds() {
        return Collections.unmodifiableSet(trainerIds);
    }
    public Set<String> getSessionCategories() {
        return Collections.unmodifiableSet(sessionCategories);
    }
}
