package unit.domain.sessions.utils;

import com.faroc.gymanager.domain.sessions.SessionCategory;
import com.faroc.gymanager.domain.shared.time.TimeUtils;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.faroc.gymanager.domain.sessions.SessionCategory.*;

public class SessionConstants {
    public static final LocalDate DATE_DEFAULT = TimeUtils.toLocalDateUtcFromInstant(Instant.now());

    public static final TimeSlot SESSION_TIMESLOT_DEFAULT = new TimeSlot(
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS)
    );
    public static final String NAME_DEFAULT = "Best Session";
    public static final String DESCRIPTION_DEFAULT = "Best session right here.";
    public static final SessionCategory CATEGORY_DEFAULT = FUNCTIONAL;
    public static final int MAX_PARTICIPANTS_DEFAULT = 1;
}
