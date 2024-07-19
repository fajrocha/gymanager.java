package com.faroc.gymanager.domain.sessions.errors;

import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;

import java.util.UUID;

public class SessionErrors {
    public static String ROOM_NOT_FOUND = "Room was not found.";
    public static String roomNotFound(UUID roomId) {
        return "Failed to add session. Room with id " + roomId + " not found.";
    }

    public static String TRAINER_NOT_FOUND = "Trainer was not found.";
    public static String trainerNotFound(UUID trainerId) {
        return "Failed to add session. Trainer with id " + trainerId + " not found.";
    }

    public static String TRAINER_SCHEDULE_CONFLICT = "Trainer is not available during the time range selected.";
    public static String trainerScheduleConflict(UUID trainerId, TimeSlot timeSlot) {
        return "Failed to add session. Trainer with id " + trainerId + " is already busy during " +
                "the time selected between " + timeSlot.getStartTime()  + " and " + timeSlot.getEndTime() + ".";
    }
}