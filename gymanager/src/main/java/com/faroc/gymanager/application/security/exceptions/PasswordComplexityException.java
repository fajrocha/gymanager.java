package com.faroc.gymanager.application.security.exceptions;

public class PasswordComplexityException extends RuntimeException {
    public static final String DEFAULT_DETAIL = "Password is not strong enough.";
    private String detail = DEFAULT_DETAIL;

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
