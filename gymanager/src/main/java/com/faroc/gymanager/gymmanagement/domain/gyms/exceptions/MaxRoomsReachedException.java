package com.faroc.gymanager.gymmanagement.domain.gyms.exceptions;

import com.faroc.gymanager.gymmanagement.domain.gyms.errors.GymsErrors;
import lombok.Getter;

@Getter
public class MaxRoomsReachedException extends RuntimeException {
    private final String detail = GymsErrors.MAX_ROOMS_REACHED;

    public MaxRoomsReachedException(String message) {
        super(message);
    }
}
