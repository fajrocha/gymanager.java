package com.faroc.gymanager.application.shared.exceptions;

public class AuthException extends RuntimeException {
    private String detail = "Authentication or authorization failed.";

    public AuthException(String message) {
        super(message);
    }
    public AuthException(String message, String detail) {

        super(message);
        this.detail = detail;
    }
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
    public AuthException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
