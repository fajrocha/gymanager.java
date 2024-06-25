package com.faroc.gymanager.domain.shared.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtils {
    public static LocalDate toLocalDateUtcFromInstant(Instant instant) {
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        return zonedDateTime.toLocalDate();
    }
}
