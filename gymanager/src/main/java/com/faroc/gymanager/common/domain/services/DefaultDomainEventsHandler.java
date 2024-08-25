package com.faroc.gymanager.common.domain.services;

import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.common.domain.AggregateRoot;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DefaultDomainEventsHandler implements DomainEventsPublisher {
    private final ApplicationEventPublisher publisher;

    public DefaultDomainEventsHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishEventsFromAggregate(AggregateRoot root) {
        while (root.hasDomainEvents()) {
            var event = root.popEvent();
            publisher.publishEvent(event);
        }
    }
}
