package com.faroc.gymanager.common.infrastructure.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.modulith.events.CompletedEventPublications;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class EventsPublications {
    @Value("${domain.events.republish-older-than-seconds}")
    private long republishEventsOlderThan;

    @Value("${domain.events.clear-older-than-seconds}")
    private long clearEventsOlderThan;

    private final IncompleteEventPublications incompleteEvents;
    private final CompletedEventPublications completeEvents;

    @Autowired
    public EventsPublications(IncompleteEventPublications incompleteEvents, CompletedEventPublications completeEvents) {
        this.incompleteEvents = incompleteEvents;
        this.completeEvents = completeEvents;
    }

    public void resubmitUnpublishedEvents() {
        incompleteEvents.resubmitIncompletePublicationsOlderThan(Duration.ofSeconds(republishEventsOlderThan));
    }

    public void clearPublishedEvents() {
        completeEvents.deletePublicationsOlderThan(Duration.ofSeconds(clearEventsOlderThan));
    }
}
