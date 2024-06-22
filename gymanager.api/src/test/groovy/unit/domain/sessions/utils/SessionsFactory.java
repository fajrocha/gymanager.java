package unit.domain.sessions.utils;

import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.TimeSlot;

import java.time.LocalDate;
import java.util.UUID;

public class SessionsFactory {
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
