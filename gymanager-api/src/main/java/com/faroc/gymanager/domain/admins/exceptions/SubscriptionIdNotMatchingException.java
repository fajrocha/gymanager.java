package com.faroc.gymanager.domain.admins.exceptions;

import com.faroc.gymanager.domain.admins.errors.AdminErrors;

public class SubscriptionIdNotMatchingException extends RuntimeException {
    private String detail = AdminErrors.SUBSCRIPTION_ID_NOT_MATCHING;

    public SubscriptionIdNotMatchingException(String message) {
        super(message);
    }

    public SubscriptionIdNotMatchingException(String message, String detail) {

        super(message);
        this.detail = detail;
    }

    public SubscriptionIdNotMatchingException(String message, Throwable cause, String detail) {

        super(message, cause);
        this.detail = detail;
    }

    public SubscriptionIdNotMatchingException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
