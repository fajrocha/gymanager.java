package com.faroc.gymanager.sessionmanagement.domain.sessions.exceptions;

import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionErrors;
import lombok.Getter;

@Getter
public class MaxParticipantsReachedException extends RuntimeException {
    private final String detail = SessionErrors.MAX_PARTICIPANTS_REACHED;

    public MaxParticipantsReachedException(String message) {
        super(message);
    }
}
