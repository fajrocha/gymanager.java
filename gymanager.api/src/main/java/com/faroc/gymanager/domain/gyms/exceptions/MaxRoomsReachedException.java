package com.faroc.gymanager.domain.gyms.exceptions;

import com.faroc.gymanager.domain.gyms.errors.GymsErrors;
import com.faroc.gymanager.domain.sessions.SessionErrors;
import lombok.Getter;

@Getter
public class MaxRoomsReachedException extends RuntimeException {
    private final String detail = GymsErrors.MAX_ROOMS_REACHED;

    public MaxRoomsReachedException(String message) {
        super(message);
    }
}
