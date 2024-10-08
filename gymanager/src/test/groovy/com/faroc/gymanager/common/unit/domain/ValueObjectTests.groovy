package com.faroc.gymanager.common.unit.domain


import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

class ValueObjectTests extends Specification {

    final EPOCH_SECONDS = 1719095117

    Instant startTime
    Instant endTime
    TimeSlot timeRange

    def setup() {
        startTime = Instant.ofEpochSecond(EPOCH_SECONDS)
        endTime = Instant.ofEpochSecond(EPOCH_SECONDS).plus(1, ChronoUnit.HOURS)
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
        def endTime = Instant.ofEpochSecond(EPOCH_SECONDS).plus(2, ChronoUnit.HOURS)
        def anotherTimeRange = new TimeSlot(startTime, endTime)

        then:
        timeRange != anotherTimeRange
        timeRange.hashCode() != anotherTimeRange.hashCode()
    }
}
