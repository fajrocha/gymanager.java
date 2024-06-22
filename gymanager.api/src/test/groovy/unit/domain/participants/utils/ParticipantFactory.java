package unit.domain.participants.utils;

import com.faroc.gymanager.domain.participants.Participant;

import java.util.UUID;

public class ParticipantFactory {
    public static Participant create(UUID userId) {
        return new Participant(userId);
    }
}
