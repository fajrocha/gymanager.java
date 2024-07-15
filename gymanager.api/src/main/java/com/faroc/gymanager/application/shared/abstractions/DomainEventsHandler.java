package com.faroc.gymanager.application.shared.abstractions;

import com.faroc.gymanager.domain.shared.AggregateRoot;

public interface DomainEventsHandler {
    void publishEvents(AggregateRoot root);
}
