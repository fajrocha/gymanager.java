package com.faroc.gymanager.domain.gyms.exceptions;

import com.faroc.gymanager.domain.gyms.errors.GymsErrors;
import lombok.Getter;

@Getter
public class MaxTrainersReachedException extends RuntimeException {
    private final String detail = GymsErrors.MAX_ROOMS_REACHED;

    public MaxTrainersReachedException(String message) {
        super(message);
    }
}
