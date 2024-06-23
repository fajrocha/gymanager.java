package com.faroc.gymanager.domain.schedules;

import com.faroc.gymanager.domain.schedules.errors.ScheduleErrors;
import com.faroc.gymanager.domain.timeslots.TimeSlot;
import com.faroc.gymanager.domain.shared.TimeUtils;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import com.faroc.gymanager.domain.shared.strategicpatterns.Entity;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

@Getter
public class Schedule extends Entity {
    private final Map<LocalDate, Set<TimeSlot>> calendar = new HashMap<>();

    public Schedule() {
    }

    public Schedule(UUID id) {
        super(id);
    }

    public void makeReservation(TimeSlot timeSlot) {
        LocalDate localDate = TimeUtils.toLocalDateUtcFromInstant(timeSlot.getStartTime());

        if (!calendar.containsKey(localDate)) {
            calendar.put(localDate, new HashSet<>(List.of(timeSlot)));
            return;
        }

        var slotsReserved = calendar.get(localDate);

        if (anyOverlap(slotsReserved, timeSlot))
            throw new ConflictException(
                    ScheduleErrors.timeslotOverlap(timeSlot),
                    ScheduleErrors.TIMESLOT_OVERLAP
            );

        slotsReserved.add(timeSlot);
    }

    public void updateReservation(TimeSlot previousTimeSlot, TimeSlot newTimeSlot) {
        cancelReservation(previousTimeSlot);
        makeReservation(newTimeSlot);
    }

    public boolean hasReservation(TimeSlot timeSlot) {
        LocalDate localDate = TimeUtils.toLocalDateUtcFromInstant(timeSlot.getStartTime());

        if (!calendar.containsKey(localDate))
            return false;

        var slotsReserved = calendar.get(localDate);

        return slotsReserved.contains(timeSlot);
    }

    public void cancelReservation(TimeSlot timeSlot) {
        LocalDate localDate = TimeUtils.toLocalDateUtcFromInstant(timeSlot.getStartTime());

        if (!calendar.containsKey(localDate))
            throw new UnexpectedException(
                    ScheduleErrors.reservationDateNotFound(localDate),
                    ScheduleErrors.RESERVATION_DATE_NOT_FOUND
            );

        var slotsReserved = calendar.get(localDate);

        if (!slotsReserved.remove(timeSlot))
            throw new UnexpectedException(
                    ScheduleErrors.reservationTimeslotNotFound(timeSlot),
                    ScheduleErrors.RESERVATION_TIMESLOT_NOT_FOUND
            );
    }

    private boolean anyOverlap(Set<TimeSlot> slotsReserved, TimeSlot timeSlot) {
        return slotsReserved.stream().anyMatch(t -> t.overlapsWith(timeSlot));
    }

    public static Schedule createEmpty() {
        return new Schedule();
    }
}
