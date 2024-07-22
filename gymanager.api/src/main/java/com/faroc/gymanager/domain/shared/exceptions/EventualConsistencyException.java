package com.faroc.gymanager.domain.shared.exceptions;

public class EventualConsistencyException extends RuntimeException {
    private final String detail = "Eventual consistency error has occurred.";

    public EventualConsistencyException(String message) {
        super(message);
    }

    public EventualConsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventualConsistencyException(Throwable cause) {
        super(cause);
    }
}
