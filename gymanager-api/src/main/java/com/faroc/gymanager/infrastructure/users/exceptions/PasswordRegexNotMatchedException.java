package com.faroc.gymanager.infrastructure.users.exceptions;

public class PasswordRegexNotMatchedException extends RuntimeException {
    private String detail = "Password is not strong enough.";

    public PasswordRegexNotMatchedException(String message) {
        super(message);
    }

    public PasswordRegexNotMatchedException(String message, String detail) {

        super(message);
        this.detail = detail;
    }

    public PasswordRegexNotMatchedException(String message, Throwable cause, String detail) {

        super(message, cause);
        this.detail = detail;
    }

    public PasswordRegexNotMatchedException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
