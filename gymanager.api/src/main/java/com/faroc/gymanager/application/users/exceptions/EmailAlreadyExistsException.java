package com.faroc.gymanager.application.users.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    private String detail = "Email already exists.";

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
    public EmailAlreadyExistsException(String message, String detail) {

        super(message);
        this.detail = detail;
    }
    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public EmailAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
