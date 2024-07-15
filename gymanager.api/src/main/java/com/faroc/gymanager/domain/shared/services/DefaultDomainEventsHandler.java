package com.faroc.gymanager.domain.shared.services;

import com.faroc.gymanager.application.shared.abstractions.DomainEventsHandler;
import com.faroc.gymanager.domain.shared.AggregateRoot;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DefaultDomainEventsHandler implements DomainEventsHandler {
    private final ApplicationEventPublisher publisher;

    public DefaultDomainEventsHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishEvents(AggregateRoot root) {
        while (root.hasDomainEvents()) {
            publisher.publishEvent(root.popEvent());
        }
    }
}
