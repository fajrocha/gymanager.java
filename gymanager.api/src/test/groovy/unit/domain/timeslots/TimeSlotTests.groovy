package unit.domain.timeslots

import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot
import spock.lang.Specification
import java.time.Instant
import java.time.temporal.ChronoUnit

import static unit.domain.testutils.TimeProvider.EPOCH_SECONDS


class TimeSlotTests extends Specification {
    TimeSlot timeSlot

    def setup() {
        def END_TIME_DELTA = 1
        def startTime = Instant.ofEpochSecond(EPOCH_SECONDS)
        def endTime = Instant.ofEpochSecond(EPOCH_SECONDS).plus(END_TIME_DELTA, ChronoUnit.HOURS)

        timeSlot = new TimeSlot(startTime,
                endTime)
    }

    def "when time slots overlap should return true"() {
        when:
        def minutes = 30
        def hours = 1
        def startTime = Instant.ofEpochSecond(EPOCH_SECONDS)
                .plus(minutes, ChronoUnit.MINUTES)
        def endTime = Instant.ofEpochSecond(EPOCH_SECONDS)
                .plus(hours, ChronoUnit.HOURS)
                .plus(minutes, ChronoUnit.MINUTES)
        def anotherTimeSlot = new TimeSlot(startTime,
                endTime)

        then:
        timeSlot.overlapsWith(anotherTimeSlot)
    }

    def "when time slots dont overlap should return false"(int startTimeDelta, int endTimeDelta) {
        when:
        def startTime = Instant.ofEpochSecond(EPOCH_SECONDS)
                .plus(startTimeDelta, ChronoUnit.HOURS)
        def endTime = Instant.ofEpochSecond(EPOCH_SECONDS)
                .plus(endTimeDelta, ChronoUnit.HOURS)
        def anotherTimeSlot = new TimeSlot(startTime,
                endTime)

        then:
        !timeSlot.overlapsWith(anotherTimeSlot)

        where:
        startTimeDelta | endTimeDelta
        1              | 2
        -1             | 0
    }
}
