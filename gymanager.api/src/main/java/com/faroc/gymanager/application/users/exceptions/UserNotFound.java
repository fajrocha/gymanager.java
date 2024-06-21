package com.faroc.gymanager.application.users.exceptions;

public class UserNotFound extends RuntimeException {
    private String detail = "User not found.";

    public UserNotFound(String message) {
        super(message);
    }
    public UserNotFound(String message, String detail) {

        super(message);
        this.detail = detail;
    }
    public UserNotFound(String message, Throwable cause) {
        super(message, cause);
    }
    public UserNotFound(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
