package com.faroc.gymanager.common.application.abstractions;

import com.faroc.gymanager.common.domain.AggregateRoot;

public interface DomainEventsPublisher {
    void publishEventsFromAggregate(AggregateRoot aggregateRoot);
}
