package com.faroc.gymanager.domain.shared.abstractions;

import com.faroc.gymanager.domain.shared.events.DomainEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayDeque;
import java.util.Queue;

@RequestScope
@Component
public class DomainEventsTracker {
    private final Queue<DomainEvent> events = new ArrayDeque<>();

    public void addEvent(DomainEvent event) {
        events.add(event);
    }

    public DomainEvent popEvent() {
        return events.poll();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public boolean hasContent() {
        return !isEmpty();
    }
}
