package com.faroc.gymanager.domain.shared.time;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.*;

public class TimeUtils {
    private static final String TIMEZONE = "UTC";

    public static LocalDate toLocalDateUtcFromInstant(Instant instant) {
        ZonedDateTime zonedDateTime = getZonedDateTimeFromInstant(instant);

        return zonedDateTime.toLocalDate();
    }

    public static OffsetDateTime toOffsetDateTimeFromInstant(Instant instant) {
        return OffsetDateTime.ofInstant(instant, ZoneId.of(TIMEZONE));
    }

    private static ZonedDateTime getZonedDateTimeFromInstant(Instant instant) {
        ZoneId zoneId = ZoneId.of(TIMEZONE);

        return instant.atZone(zoneId);
    }
}
