package unit.domain.sessions.utils;

import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.sessions.SessionCategory;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import net.datafaker.Faker;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static unit.domain.sessions.utils.SessionConstants.*;

public class SessionsFactory {

    public static Session create(int maxNumberParticipants) {
        return new Session(
                UUID.randomUUID(),
                SESSION_TIMESLOT,
                SESSION_NAME,
                SESSION_DESCRIPTION,
                SESSION_CATEGORIES,
                maxNumberParticipants);
    }

    public static Session create(TimeSlot timeSlot, int maxNumberParticipants) {
        return new Session(
                UUID.randomUUID(),
                timeSlot,
                SESSION_NAME,
                SESSION_DESCRIPTION,
                SESSION_CATEGORIES,
                maxNumberParticipants);
    }

    public static Session create(UUID id, TimeSlot timeSlot, int maxNumberParticipants) {
        return new Session(
                id,
                UUID.randomUUID(),
                timeSlot,
                SESSION_NAME,
                SESSION_DESCRIPTION,
                SESSION_CATEGORIES,
                maxNumberParticipants);
    }
}