package com.faroc.gymanager.domain.shared.entities.schedules;

import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;

import java.time.LocalDate;

public class ScheduleErrors {
    public static String RESERVATION_DATE_NOT_FOUND = "Reservation not found.";
    public static String reservationDateNotFound(LocalDate date) {
        return "Reservation on the " + date + " not found.";
    }

    public static String RESERVATION_TIMESLOT_NOT_FOUND = "Timeslot not found.";
    public static String reservationTimeslotNotFound(TimeSlot timeSlot) {
        return "Timeslot between " + timeSlot.getStartTime() + " and " + timeSlot.getEndTime() + " not found.";
    }
    public static String TIMESLOT_OVERLAP = "Timeslot already reserved.";
    public static String timeslotOverlap(TimeSlot timeSlot) {
      return "Timeslot between " + timeSlot.getStartTime() + " and " + timeSlot.getEndTime() + " already reserved.";
    }
}
