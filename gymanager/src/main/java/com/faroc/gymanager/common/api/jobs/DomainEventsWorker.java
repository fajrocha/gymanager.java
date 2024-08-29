package com.faroc.gymanager.common.api.jobs;

import com.faroc.gymanager.common.infrastructure.events.EventsPublications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class DomainEventsWorker {
    private final EventsPublications eventsPublications;

    @Autowired
    public DomainEventsWorker(EventsPublications eventsPublications) {
        this.eventsPublications = eventsPublications;
    }

    @Scheduled(fixedRateString = "#{${domain.events.republish-periodicity-seconds} * 1000}")
    public void performTask() {
        log.info("Republishing any incomplete domain events.");
        eventsPublications.resubmitUnpublishedEvents();

        log.info("Clearing completed domain events.");
        eventsPublications.clearPublishedEvents();
    }
}
