package com.faroc.gymanager.common.application.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private String detail = "Resource requested not found.";

    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String message, String detail) {

        super(message);
        this.detail = detail;
    }
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
