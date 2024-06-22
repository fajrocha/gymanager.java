package unit.domain.sessions

import com.faroc.gymanager.domain.participants.Participant
import com.faroc.gymanager.domain.sessions.Session
import com.faroc.gymanager.domain.sessions.errors.SessionErrors
import com.faroc.gymanager.domain.sessions.exceptions.CancellationTooCloseToSession
import com.faroc.gymanager.domain.sessions.exceptions.MaxParticipantsReachedException
import com.faroc.gymanager.domain.shared.TimeSlot
import com.faroc.gymanager.domain.shared.abstractions.InstantProvider
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException
import spock.lang.Specification
import unit.domain.participants.utils.ParticipantFactory
import unit.domain.sessions.utils.SessionsFactory

import java.time.Instant
import java.time.temporal.ChronoUnit

class SessionsTests extends Specification {

    Participant participant
    Session session
    InstantProvider instantProvider

    def setup() {
        def participantId = UUID.randomUUID()
        def maxParticipants = 1
        participant = ParticipantFactory.create(participantId)

        def timeRange = new TimeSlot(
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.HOURS)
        )

        instantProvider = Mock(InstantProvider)

        session = SessionsFactory.create(
                timeRange,
                maxParticipants
        )
    }

    def "when participant makes reservation and session not full should add participant"() {
        when:
        session.makeReservation(participant)

        then:
        session.hasParticipant(participant)
    }

    def "when participant makes reservation but session is full should throw max participants exception"() {
        given:
        def anotherParticipant = ParticipantFactory.create(UUID.randomUUID())
        session.makeReservation(participant)

        when:
        session.makeReservation(anotherParticipant)

        then:
        def ex = thrown(MaxParticipantsReachedException)
        ex.getDetail() == SessionErrors.MAX_PARTICIPANTS_REACHED
    }

    def "when participant cancels reservation too close to session start should throw exception"() {
        given:
        def deltaHours = Session.MIN_CANCELLATION_HOURS - 12
        session.makeReservation(participant)

        instantProvider.now() >> Instant.now().minus(deltaHours, ChronoUnit.HOURS)

        when:
        session.cancelReservation(participant, instantProvider)

        then:
        def ex = thrown(CancellationTooCloseToSession)
        ex.getDetail() == SessionErrors.CANCELLATION_CLOSE_TO_START
    }

    def "when participant cancels reservation on time should cancel reservation"() {
        given:
        def deltaHours = Session.MIN_CANCELLATION_HOURS + 12
        session.makeReservation(participant)

        instantProvider.now() >> Instant.now().minus(deltaHours, ChronoUnit.HOURS)

        when:
        session.cancelReservation(participant, instantProvider)

        then:
        !session.hasParticipant(participant)
    }

    def "when participant cancels reservation but does not have a reservation should throw unexpected exception"() {
        given:
        def deltaHours = Session.MIN_CANCELLATION_HOURS + 12
        instantProvider.now() >> Instant.now().minus(deltaHours, ChronoUnit.HOURS)

        when:
        session.cancelReservation(participant, instantProvider)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.PARTICIPANT_NOT_FOUND
    }
}
