package com.faroc.gymanager.domain.shared;

import com.faroc.gymanager.domain.shared.events.DomainEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class AggregateRoot extends Entity {
    protected final List<DomainEvent> domainEvents = new ArrayList<>();

    public AggregateRoot() {
    }

    public AggregateRoot(UUID id) {
        super(id);
    }
}
