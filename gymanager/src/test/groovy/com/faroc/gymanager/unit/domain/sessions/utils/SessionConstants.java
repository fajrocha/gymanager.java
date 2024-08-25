package com.faroc.gymanager.unit.domain.sessions.utils;

import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class SessionConstants {
    public static final TimeSlot SESSION_TIMESLOT_DEFAULT = TimeSlot.fromInstants(
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS)
    );

    public static final String NAME_DEFAULT = "Best Session";
    public static final String DESCRIPTION_DEFAULT = "Best session right here.";
    public static final String CATEGORY_DEFAULT = "Functional";
    public static final int MAX_PARTICIPANTS_DEFAULT = 1;
}
