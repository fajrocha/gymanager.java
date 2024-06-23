package com.faroc.gymanager.domain.sessions.exceptions;

import com.faroc.gymanager.domain.sessions.errors.SessionErrors;
import lombok.Getter;

@Getter
public class MaxParticipantsReachedException extends RuntimeException {
    private final String detail = SessionErrors.MAX_PARTICIPANTS_REACHED;

    public MaxParticipantsReachedException(String message) {
        super(message);
    }
}
