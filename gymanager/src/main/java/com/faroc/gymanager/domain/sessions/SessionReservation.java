package com.faroc.gymanager.domain.sessions;

import com.faroc.gymanager.domain.shared.Entity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SessionReservation extends Entity {
    private final UUID participantId;

    public SessionReservation(UUID participantId) {
        this.participantId = participantId;
    }

    public SessionReservation(UUID id, UUID participantId) {
        super(id);
        this.participantId = participantId;
    }
}
