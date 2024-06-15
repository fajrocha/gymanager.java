package com.faroc.gymanager.domain.admins.exceptions;

import com.faroc.gymanager.domain.admins.errors.AdminErrors;

public class SubscriptionIdNotMatching extends RuntimeException {
    private String detail = AdminErrors.SUBSCRIPTION_ID_NOT_MATCHING;

    public SubscriptionIdNotMatching(String message) {
        super(message);
    }

    public SubscriptionIdNotMatching(String message, String detail) {

        super(message);
        this.detail = detail;
    }

    public SubscriptionIdNotMatching(String message, Throwable cause, String detail) {

        super(message, cause);
        this.detail = detail;
    }

    public SubscriptionIdNotMatching(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
