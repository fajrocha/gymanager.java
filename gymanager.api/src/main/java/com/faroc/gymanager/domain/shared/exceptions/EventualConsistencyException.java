package com.faroc.gymanager.domain.shared.exceptions;

public class EventualConsistencyException extends RuntimeException {
    private String detail = "Eventual consistency error has occurred.";

    public EventualConsistencyException(String message) {
        super(message);
    }

    public EventualConsistencyException(Throwable cause) {
        super(cause);
    }
}
