package com.faroc.gymanager.domain.schedules;

import com.faroc.gymanager.domain.shared.TimeSlot;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.shared.strategicpatterns.Entity;
import lombok.Getter;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Getter
public class Schedule extends Entity {
    private final Map<LocalDate, Set<TimeSlot>> calendar = new HashMap<>();

    public Schedule() {
    }

    public Schedule(UUID id) {
        super(id);
    }

    public void reserveTimeSlot(TimeSlot timeSlot) {
        LocalDate localDate = getLocalDateFromInstant(timeSlot.getStartTime());

        if (!calendar.containsKey(localDate)) {
            calendar.put(localDate, new HashSet<>(List.of(timeSlot)));
            return;
        }

        var slotsReserved = calendar.get(localDate);

        if (anyOverlap(slotsReserved, timeSlot))
            throw new ConflictException("", "");

        slotsReserved.add(timeSlot);
    }

    private boolean anyOverlap(Set<TimeSlot> slotsReserved, TimeSlot timeSlot) {
        return slotsReserved.stream().anyMatch(t -> t.overlapsWith(timeSlot));
    }

    private LocalDate getLocalDateFromInstant(Instant instant) {
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        return zonedDateTime.toLocalDate();
    }

    public static Schedule empty() {
        return new Schedule();
    }
}
