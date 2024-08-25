package com.faroc.gymanager.gymmanagement.domain.rooms;

import com.faroc.gymanager.common.domain.AggregateRoot;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Room extends AggregateRoot {

    private final UUID gymId;
    private final int maxDailySessions;
    private final String name;

    public Room(UUID gymId, String name, int maxDailySessions) {
        this.gymId = gymId;
        this.maxDailySessions = maxDailySessions;
        this.name = name;
    }

    public Room(
            UUID id,
            UUID gymId,
            String name,
            int maxDailySessions) {
        super(id);
        this.gymId = gymId;
        this.maxDailySessions = maxDailySessions;
        this.name = name;
    }
}

