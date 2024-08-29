package com.faroc.gymanager.sessionmanagement.unit.domain.sessions.utils;

import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot;

import java.util.UUID;


public class SessionsTestsFactory {
    public static Session create() {
        return create(SessionConstants.MAX_PARTICIPANTS_DEFAULT);
    }

    public static Session create(UUID id) {
        return new Session(
                id,
                UUID.randomUUID(),
                SessionConstants.SESSION_TIMESLOT_DEFAULT,
                SessionConstants.NAME_DEFAULT,
                SessionConstants.DESCRIPTION_DEFAULT,
                SessionConstants.CATEGORY_DEFAULT,
                SessionConstants.MAX_PARTICIPANTS_DEFAULT);
    }

    public static Session create(TimeSlot timeSlot) {
        return new Session(
                UUID.randomUUID(),
                UUID.randomUUID(),
                timeSlot,
                SessionConstants.NAME_DEFAULT,
                SessionConstants.DESCRIPTION_DEFAULT,
                SessionConstants.CATEGORY_DEFAULT,
                SessionConstants.MAX_PARTICIPANTS_DEFAULT);
    }

    public static Session create(int maxNumberParticipants) {
        return new Session(
                UUID.randomUUID(),
                UUID.randomUUID(),
                SessionConstants.SESSION_TIMESLOT_DEFAULT,
                SessionConstants.NAME_DEFAULT,
                SessionConstants.DESCRIPTION_DEFAULT,
                SessionConstants.CATEGORY_DEFAULT,
                maxNumberParticipants);
    }

    public static Session create(TimeSlot timeSlot, int maxNumberParticipants) {
        return new Session(
                UUID.randomUUID(),
                UUID.randomUUID(),
                timeSlot,
                SessionConstants.NAME_DEFAULT,
                SessionConstants.DESCRIPTION_DEFAULT,
                SessionConstants.CATEGORY_DEFAULT,
                maxNumberParticipants);
    }

    public static Session create(UUID id, TimeSlot timeSlot, int maxNumberParticipants) {
        return new Session(
                id,
                UUID.randomUUID(),
                UUID.randomUUID(),
                timeSlot,
                SessionConstants.NAME_DEFAULT,
                SessionConstants.DESCRIPTION_DEFAULT,
                SessionConstants.CATEGORY_DEFAULT,
                maxNumberParticipants);
    }
}