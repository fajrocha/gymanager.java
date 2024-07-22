package com.faroc.gymanager.domain.shared.time;

import java.time.*;

public class TimeUtils {
    private static final String TIMEZONE = "UTC";

    public static LocalDate toLocalDateUtcFromInstant(Instant instant) {
        ZonedDateTime zonedDateTime = getZonedDateTimeFromInstant(instant);

        return zonedDateTime.toLocalDate();
    }

    public static LocalTime toLocalTimeUtcFromInstant(Instant instant) {
        ZonedDateTime zonedDateTime = getZonedDateTimeFromInstant(instant);

        return zonedDateTime.toLocalTime();
    }

    private static ZonedDateTime getZonedDateTimeFromInstant(Instant instant) {
        ZoneId zoneId = ZoneId.of(TIMEZONE);

        return instant.atZone(zoneId);
    }
}
