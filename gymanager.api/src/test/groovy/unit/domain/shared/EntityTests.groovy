package unit.domain.shared

import com.faroc.gymanager.domain.sessions.Session
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot
import spock.lang.Specification
import unit.domain.sessions.utils.SessionsFactory

import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class EntityTests extends Specification {

    UUID id
    Session session
    TimeSlot timeRange
    LocalDate date

    def setup() {
        def startTime = Instant.now()
        def endTime = Instant.now().plus(1, ChronoUnit.HOURS)
        timeRange = new TimeSlot(startTime, endTime)
        id = UUID.randomUUID()

        session = SessionsFactory.create(
                id,
                timeRange,
                1
        )

    }

    def "when 2 entity ids match they should be equal and hashcode should match"() {
        when:
        def anotherSession = SessionsFactory.create(
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
        def anotherSession = SessionsFactory.create(
                UUID.randomUUID(),
                timeRange,
                2
        )

        then:
        session != anotherSession
        session.hashCode() != anotherSession.hashCode()
    }
}
