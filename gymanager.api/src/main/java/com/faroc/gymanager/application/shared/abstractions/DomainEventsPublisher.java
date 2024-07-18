package com.faroc.gymanager.application.shared.abstractions;

import com.faroc.gymanager.domain.shared.AggregateRoot;

public interface DomainEventsPublisher {
    void publishEventsFromAggregate(AggregateRoot root);
}
