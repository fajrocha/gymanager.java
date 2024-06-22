package com.faroc.gymanager.domain.shared;

import com.faroc.gymanager.domain.shared.strategicpatterns.ValueObject;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Getter
public class TimeSlot extends ValueObject {
    private final Instant startTime;
    private final Instant endTime;

    public TimeSlot(Instant startTime, Instant endTime) {
        if (startTime.isAfter(endTime) || startTime == endTime)
            throw new IllegalArgumentException("Start time cannot be equal or after end time.");

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean overlapsWith(TimeSlot other) {
        var otherEndTime = other.getEndTime();
        var otherStartTime = other.getStartTime();

        if (startTime.isAfter(otherEndTime) || otherStartTime.equals(endTime))
            return false;

        if (otherStartTime.isAfter(endTime) || otherEndTime.equals(startTime))
            return false;

        return true;
    }

    @Override
    public List<Object> getEqualityComponents() {
        return List.of(
                startTime,
                endTime
        );
    }
}
