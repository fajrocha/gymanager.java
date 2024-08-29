package com.faroc.gymanager.common.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Entity {

    protected final UUID id;

    protected Entity() {
        this.id = UUID.randomUUID();
    }

    protected Entity(UUID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        return ((Entity) obj).getId() == id ;
    }
}
