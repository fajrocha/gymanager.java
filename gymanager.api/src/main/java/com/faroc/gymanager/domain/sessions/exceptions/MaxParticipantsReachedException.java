package com.faroc.gymanager.domain.sessions.exceptions;

import com.faroc.gymanager.domain.sessions.errors.SessionErrors;
import lombok.Getter;

@Getter
public class MaxParticipantsReachedException extends RuntimeException {
    private String detail = SessionErrors.MAX_PARTICIPANTS_REACHED;

    public MaxParticipantsReachedException(String message) {
        super(message);
    }

    public MaxParticipantsReachedException(String message, String detail) {

        super(message);
        this.detail = detail;
    }

    public MaxParticipantsReachedException(String message, Throwable cause, String detail) {

        super(message, cause);
        this.detail = detail;
    }

    public MaxParticipantsReachedException(Throwable cause) {
        super(cause);
    }
}
