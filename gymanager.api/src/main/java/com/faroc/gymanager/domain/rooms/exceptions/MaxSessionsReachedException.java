package com.faroc.gymanager.domain.rooms.exceptions;

import com.faroc.gymanager.domain.rooms.errors.RoomErrors;
import lombok.Getter;

@Getter
public class MaxSessionsReachedException extends RuntimeException {
    private final String detail = RoomErrors.MAX_SESSIONS_REACHED;

    public MaxSessionsReachedException(String message) {
        super(message);
    }
}
