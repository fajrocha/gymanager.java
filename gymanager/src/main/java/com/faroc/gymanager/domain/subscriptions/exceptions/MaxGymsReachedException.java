package com.faroc.gymanager.domain.subscriptions.exceptions;

import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors;
import lombok.Getter;

@Getter
public class MaxGymsReachedException extends RuntimeException {
    private String detail = SubscriptionErrors.MAX_GYMS_REACHED;

    public MaxGymsReachedException(String message) {
        super(message);
    }

    public MaxGymsReachedException(String message, String detail) {

        super(message);
        this.detail = detail;
    }

    public MaxGymsReachedException(String message, Throwable cause, String detail) {

        super(message, cause);
        this.detail = detail;
    }

    public MaxGymsReachedException(Throwable cause) {
        super(cause);
    }
}
