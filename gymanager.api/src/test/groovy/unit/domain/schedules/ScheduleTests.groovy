package unit.domain.schedules

import com.faroc.gymanager.domain.schedules.Schedule
import com.faroc.gymanager.domain.schedules.errors.ScheduleErrors
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException
import com.faroc.gymanager.domain.timeslots.TimeSlot
import com.faroc.gymanager.domain.shared.TimeUtils
import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit
import static unit.domain.testutils.TimeProvider.EPOCH_SECONDS

class ScheduleTests extends Specification {

    Instant startTime
    Instant endTime
    TimeSlot timeSlot

    Schedule schedule

    def setup() {
        startTime = Instant.ofEpochSecond(EPOCH_SECONDS)
        endTime = Instant.ofEpochSecond(EPOCH_SECONDS).plus(1, ChronoUnit.HOURS)
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
        def day = TimeUtils.toLocalDateUtcFromInstant(startTime);
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
