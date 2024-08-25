package com.faroc.gymanager.common.application.exceptions;

public class ForbiddenException extends RuntimeException {
    private String detail = "Forbidden user, not enough permissions.";

    public ForbiddenException(String message) {
        super(message);
    }
    public ForbiddenException(String message, String detail) {

        super(message);
        this.detail = detail;
    }
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
    public ForbiddenException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
