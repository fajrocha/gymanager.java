package unit.domain.shared

import com.faroc.gymanager.domain.shared.TimeSlot
import spock.lang.Specification

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class ValueObjectTests extends Specification {

    Instant startTime
    Instant endTime
    TimeSlot timeRange

    def setup() {
        startTime = Instant.now()
        endTime = Instant.now().plus(1, ChronoUnit.HOURS)
        timeRange = new TimeSlot(startTime, endTime)
    }

    def "when 2 value objects have same values should be equal and hashcode should match"() {
        when:
        def anotherTimeRange = new TimeSlot(startTime, endTime)

        then:
        timeRange == anotherTimeRange
        timeRange.hashCode() == anotherTimeRange.hashCode()
    }

    def "when 2 value objects have different values should be not be equal and hashcode should not match"() {
        when:
        def endTime = Instant.now().plus(1, ChronoUnit.HOURS)
        def anotherTimeRange = new TimeSlot(startTime, endTime)

        then:
        timeRange != anotherTimeRange
        timeRange.hashCode() != anotherTimeRange.hashCode()
    }
}
