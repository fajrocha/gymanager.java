package com.faroc.gymanager.domain.shared;

import com.faroc.gymanager.domain.shared.events.DomainEvent;

import java.util.*;

public abstract class AggregateRoot extends Entity {
    protected final Queue<DomainEvent> domainEvents = new ArrayDeque<>();

    public AggregateRoot() {
    }

    public AggregateRoot(UUID id) {
        super(id);
    }

    public DomainEvent popEvent() {
        return domainEvents.poll();
    }

    public boolean emptyDomainEvents() {
        return domainEvents.isEmpty();
    }

    public boolean hasDomainEvents() {
        return !emptyDomainEvents();
    }
}
