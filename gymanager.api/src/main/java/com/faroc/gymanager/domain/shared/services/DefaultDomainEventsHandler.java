package com.faroc.gymanager.domain.shared.services;

import com.faroc.gymanager.application.shared.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.domain.shared.AggregateRoot;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DefaultDomainEventsHandler implements DomainEventsPublisher {
    private final ApplicationEventPublisher publisher;

    public DefaultDomainEventsHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishEventsFromAggregate(AggregateRoot aggregateRoot) {
        while (aggregateRoot.hasDomainEvents()) {
            var event = aggregateRoot.popEvent();
            publisher.publishEvent(event);
        }
    }
}
