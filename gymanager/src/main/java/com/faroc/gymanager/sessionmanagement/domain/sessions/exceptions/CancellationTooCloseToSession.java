package com.faroc.gymanager.sessionmanagement.domain.sessions.exceptions;

import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionErrors;
import lombok.Getter;

@Getter
public class CancellationTooCloseToSession extends RuntimeException {
    private String detail = SessionErrors.CANCELLATION_CLOSE_TO_START;

    public CancellationTooCloseToSession(String message) {
        super(message);
    }

    public CancellationTooCloseToSession(String message, String detail) {

        super(message);
        this.detail = detail;
    }

    public CancellationTooCloseToSession(String message, Throwable cause, String detail) {

        super(message, cause);
        this.detail = detail;
    }

    public CancellationTooCloseToSession(Throwable cause) {
        super(cause);
    }
}
