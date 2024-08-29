package com.faroc.gymanager.sessionmanagement.unit.domain.schedules

import com.faroc.gymanager.sessionmanagement.domain.common.schedules.Schedule
import com.faroc.gymanager.sessionmanagement.domain.common.schedules.ScheduleErrors
import com.faroc.gymanager.common.domain.exceptions.ConflictException
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException
import com.faroc.gymanager.sessionmanagement.domain.common.time.TimeUtils
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

import static com.faroc.gymanager.sessionmanagement.unit.domain.common.TimeTestsProvider.EPOCH_SECONDS
import static java.time.Instant.ofEpochSecond

class ScheduleTests extends Specification {

    Instant startTime
    Instant endTime
    TimeSlot timeSlot

    Schedule schedule

    def setup() {
        startTime = ofEpochSecond(EPOCH_SECONDS)
        endTime = ofEpochSecond(EPOCH_SECONDS).plus(1, ChronoUnit.HOURS)
        timeSlot = new TimeSlot(startTime, endTime)
        schedule = new Schedule()
    }

    def "when making reservations and no overlap should add reservation time slot"() {
        final HOURS_DELTA = 1
        def startTimeOverlap = startTime.plus(HOURS_DELTA, ChronoUnit.HOURS)
        def endTimeOverlap = endTime.plus(HOURS_DELTA, ChronoUnit.HOURS)
        def anotherTimeSlot = new TimeSlot(startTimeOverlap, endTimeOverlap)

        when:
        schedule.makeReservation(timeSlot)
        schedule.makeReservation(anotherTimeSlot)

        then:
        def day = TimeUtils.toLocalDateUtcFromInstant(startTime)
        schedule.getCalendar().containsKey(day)
        def timeSlots = schedule.getCalendar().get(day)
        timeSlots.contains(timeSlot)
        timeSlots.contains(anotherTimeSlot)
    }

    def "when making reservations and overlaps with existing reservation should throw conflict exception"() {
        given:
        final MINUTES_OVERLAP = 30
        def startTimeOverlap = startTime.plus(MINUTES_OVERLAP, ChronoUnit.MINUTES)
        def endTimeOverlap = endTime.plus(MINUTES_OVERLAP, ChronoUnit.MINUTES)
        def anotherTimeSlot = new TimeSlot(startTimeOverlap, endTimeOverlap)

        when:
        schedule.makeReservation(timeSlot)
        schedule.makeReservation(anotherTimeSlot)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == ScheduleErrors.TIMESLOT_OVERLAP
    }

    def "when cancelling reservation but reservation date not found should throw unexpected exception"() {
        when:
        schedule.cancelReservation(timeSlot)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == ScheduleErrors.RESERVATION_DATE_NOT_FOUND
    }

    def "when cancelling reservation but reservation timeslot not found should throw unexpected exception"() {
        given:
        schedule.makeReservation(timeSlot)
        final HOURS_DELTA = 10
        def newStartTime = startTime.plus(HOURS_DELTA, ChronoUnit.HOURS)
        def newEndTime = endTime.plus(HOURS_DELTA, ChronoUnit.HOURS)
        def newTimeSlot = new TimeSlot(newStartTime, newEndTime)
        schedule.updateReservation(timeSlot, newTimeSlot)

        when:
        schedule.cancelReservation(timeSlot)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == ScheduleErrors.RESERVATION_TIMESLOT_NOT_FOUND
    }
}
