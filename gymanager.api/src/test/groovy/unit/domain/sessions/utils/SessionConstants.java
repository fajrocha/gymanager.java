package unit.domain.sessions.utils;

import com.faroc.gymanager.domain.sessions.SessionCategory;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.faroc.gymanager.domain.sessions.SessionCategory.*;

public class SessionConstants {
    public static final TimeSlot SESSION_TIMESLOT = new TimeSlot(
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS)
    );
    public static final String SESSION_NAME = "Best Session";
    public static final String SESSION_DESCRIPTION = "Best session right here.";
    public static final List<SessionCategory> SESSION_CATEGORIES = List.of(FUNCTIONAL, ZOOMBA);
    public static final int SESSION_MAX_PARTICIPANTS = 1;
}
