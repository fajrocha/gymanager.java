package com.faroc.gymanager.sessionmanagement.domain.sessions;

import com.faroc.gymanager.common.domain.Entity;
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
