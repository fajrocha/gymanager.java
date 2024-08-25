package com.faroc.gymanager.sessionmanagement.domain.trainers.errors;

import java.util.UUID;

public class TrainerErrors {
    public static final String CONFLICT_SESSION = "Session reservation already exists for this trainer.";
    public static String conflictSession(UUID sessionId, UUID trainerId) {
        return "Session reservation with id " + sessionId + "already exists for trainer " + trainerId + ".";
    }
}
