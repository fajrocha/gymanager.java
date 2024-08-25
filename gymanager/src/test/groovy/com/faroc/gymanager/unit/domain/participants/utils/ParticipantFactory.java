package com.faroc.gymanager.unit.domain.participants.utils;

import com.faroc.gymanager.sessionmanagement.domain.participants.Participant;

import java.util.UUID;

public class ParticipantFactory {
    public static Participant create() {
        return new Participant(UUID.randomUUID());
    }

    public static Participant create(UUID userId) {
        return new Participant(userId);
    }
}
