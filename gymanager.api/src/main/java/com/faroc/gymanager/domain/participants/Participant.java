package com.faroc.gymanager.domain.participants;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Participant {
    private final UUID id;
    private final UUID userId;
    private final Set<UUID> sessionsIds = new HashSet<>();

    public Participant(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
    }
}
