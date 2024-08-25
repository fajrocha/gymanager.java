package com.faroc.gymanager.common.domain;

import com.faroc.gymanager.common.domain.events.DomainEvent;

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
