package com.faroc.gymanager.common.application.exceptions;

public class UnauthorizedException extends RuntimeException {
    private String detail = "Unauthorized user.";
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
