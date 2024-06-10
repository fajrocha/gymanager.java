package com.faroc.gymanager.application.users.exceptions;

public class UnauthorizedException extends RuntimeException {
    private String detail = "Unauthorized.";

    public UnauthorizedException(String message) {
        super(message);
    }
    public UnauthorizedException(String message, String detail) {

        super(message);
        this.detail = detail;
    }
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
