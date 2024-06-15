package com.faroc.gymanager.domain.gyms;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Gym {
    private final UUID id;
    private final UUID subscriptionId;
    private final String name;

    public Gym(UUID subscriptionId, String name) {
        this.id = UUID.randomUUID();
        this.subscriptionId = subscriptionId;
        this.name = name;
    }
}
