package com.faroc.gymanager.unit.domain.shared

import com.faroc.gymanager.sessionmanagement.domain.sessions.Session
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot
import com.faroc.gymanager.unit.domain.sessions.utils.SessionsTestsFactory
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

class EntityTests extends Specification {

    UUID id
    Session session
    TimeSlot timeRange

    def setup() {
        def startTime = Instant.now()
        def endTime = Instant.now().plus(1, ChronoUnit.HOURS)
        timeRange = new TimeSlot(startTime, endTime)
        id = UUID.randomUUID()

        session = SessionsTestsFactory.create(
                id,
                timeRange,
                1
        )
    }

    def "when 2 entity ids match they should be equal and hashcode should match"() {
        when:
        def anotherSession = SessionsTestsFactory.create(
                id,
                timeRange,
                2
        )

        then:
        session == anotherSession
        session.hashCode() == anotherSession.hashCode()
    }

    def "when 2 entity ids dont match they should be not equal and hashcode should not match"() {
        when:
        def anotherSession = SessionsTestsFactory.create(
                UUID.randomUUID(),
                timeRange,
                2
        )

        then:
        session != anotherSession
        session.hashCode() != anotherSession.hashCode()
    }
}
