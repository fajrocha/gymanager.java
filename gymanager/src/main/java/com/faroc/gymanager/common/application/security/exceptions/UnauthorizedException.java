package com.faroc.gymanager.common.application.security.exceptions;

public class UnauthorizedException extends RuntimeException {
    public static String DEFAULT_DETAIL = "Unauthorized user.";
    private String detail = DEFAULT_DETAIL;

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
