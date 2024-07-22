package com.faroc.gymanager.domain.shared.time;

import java.time.*;

public class TimeUtils {
    public static LocalDate toLocalDateUtcFromInstant(Instant instant) {
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        return zonedDateTime.toLocalDate();
    }

    public static LocalDateTime toLocalDateTimeUtcFromInstant(Instant instant) {
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        return zonedDateTime.toLocalDateTime();
    }
}
