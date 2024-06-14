package com.faroc.gymanager.application.security.exceptions;

public class PasswordComplexityException extends RuntimeException {
    private String detail = "Password is not strong enough.";

    public PasswordComplexityException(String message) {
        super(message);
    }

    public PasswordComplexityException(String message, String detail) {

        super(message);
        this.detail = detail;
    }

    public PasswordComplexityException(String message, Throwable cause, String detail) {

        super(message, cause);
        this.detail = detail;
    }

    public PasswordComplexityException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
