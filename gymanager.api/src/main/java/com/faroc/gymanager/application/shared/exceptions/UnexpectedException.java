package com.faroc.gymanager.application.shared.exceptions;

public class UnexpectedException extends RuntimeException {
    private String detail = "Unexpected behavior occurred.";

    public UnexpectedException(String message) {
        super(message);
    }
    public UnexpectedException(String message, String detail) {

        super(message);
        this.detail = detail;
    }
    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnexpectedException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
