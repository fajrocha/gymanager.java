package com.faroc.gymanager.domain.sessions;

import com.faroc.gymanager.domain.shared.Entity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Reservation extends Entity {
    private final UUID participantId;

    public Reservation(UUID participantId) {
        this.participantId = participantId;
    }
}
