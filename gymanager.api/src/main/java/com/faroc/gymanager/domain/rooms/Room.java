package com.faroc.gymanager.domain.rooms;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Room {
    private final UUID id;
    private final Set<UUID> sessionsIds = new HashSet<>();

    public Room() {
        this.id = UUID.randomUUID();
    }
}
