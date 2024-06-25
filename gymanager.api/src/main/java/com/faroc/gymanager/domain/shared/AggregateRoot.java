package com.faroc.gymanager.domain.shared;

import java.util.UUID;

public abstract class AggregateRoot extends Entity {
    public AggregateRoot() {
    }

    public AggregateRoot(UUID id) {
        super(id);
    }
}
