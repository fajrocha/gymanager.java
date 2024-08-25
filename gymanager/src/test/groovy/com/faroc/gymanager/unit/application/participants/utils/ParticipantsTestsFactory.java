package com.faroc.gymanager.unit.application.participants.utils;

import com.faroc.gymanager.sessionmanagement.domain.participants.Participant;

import java.util.UUID;

public class ParticipantsTestsFactory {
    public static Participant create(UUID id) {
        return new Participant(id, UUID.randomUUID());
    }
}
