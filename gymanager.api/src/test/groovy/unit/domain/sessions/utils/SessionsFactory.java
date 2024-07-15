package unit.domain.sessions.utils;

import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;

import java.util.UUID;

import static unit.domain.sessions.utils.SessionConstants.*;

public class SessionsFactory {
    public static Session create() {
        return create(MAX_PARTICIPANTS_DEFAULT);
    }

    public static Session create(int maxNumberParticipants) {
        return new Session(
                UUID.randomUUID(),
                SESSION_TIMESLOT_DEFAULT,
                NAME_DEFAULT,
                DESCRIPTION_DEFAULT,
                CATEGORIES_DEFAULT,
                maxNumberParticipants,
                DATE_DEFAULT);
    }

    public static Session create(TimeSlot timeSlot, int maxNumberParticipants) {
        return new Session(
                UUID.randomUUID(),
                timeSlot,
                NAME_DEFAULT,
                DESCRIPTION_DEFAULT,
                CATEGORIES_DEFAULT,
                maxNumberParticipants,
                DATE_DEFAULT);
    }

    public static Session create(UUID id, TimeSlot timeSlot, int maxNumberParticipants) {
        return new Session(
                id,
                UUID.randomUUID(),
                timeSlot,
                NAME_DEFAULT,
                DESCRIPTION_DEFAULT,
                CATEGORIES_DEFAULT,
                maxNumberParticipants,
                DATE_DEFAULT);
    }
}