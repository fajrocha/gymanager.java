package com.faroc.gymanager.common.domain.exceptions;

public class ConflictException extends RuntimeException {
    private String detail = "Conflict error has occurred.";

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public ConflictException(String message, Throwable cause, String detail) {

        super(message, cause);
        this.detail = detail;
    }

    public ConflictException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
