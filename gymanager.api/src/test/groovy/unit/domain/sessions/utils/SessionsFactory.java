package unit.domain.sessions.utils;

import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class SessionsFactory {
    public static Session create(int maxNumberParticipants) {
        var startTime = Instant.now();
        var endTime = Instant.now().plus(1, ChronoUnit.HOURS);
        var timeSlot = new TimeSlot(startTime, endTime);

        return new Session(
                UUID.randomUUID(),
                timeSlot,
                maxNumberParticipants);
    }

    public static Session create(TimeSlot timeSlot, int maxNumberParticipants) {
        return new Session(
                UUID.randomUUID(),
                timeSlot,
                maxNumberParticipants);
    }

    public static Session create(UUID id, TimeSlot timeSlot, int maxNumberParticipants) {
        return new Session(
                id,
                UUID.randomUUID(),
                timeSlot,
                maxNumberParticipants);
    }
}
