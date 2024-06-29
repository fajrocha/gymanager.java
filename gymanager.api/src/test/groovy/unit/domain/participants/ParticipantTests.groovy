package unit.domain.participants

import com.faroc.gymanager.domain.participants.Participant
import com.faroc.gymanager.domain.participants.errors.ParticipantErrors
import com.faroc.gymanager.domain.sessions.Session
import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import spock.lang.Specification
import unit.domain.participants.utils.ParticipantFactory
import unit.domain.sessions.utils.SessionsFactory

class ParticipantTests extends Specification {

    final MAX_SESSION_PARTICIPANTS = 1
    Participant participant

    Session session

    def setup() {
        session = SessionsFactory.create(MAX_SESSION_PARTICIPANTS)
        participant = ParticipantFactory.create()
    }

    def "when session reservation already exists on participant should throw conflict exception"() {
        given:
        participant.makeReservation(session)

        when:
        participant.makeReservation(session)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == ParticipantErrors.CONFLICT_SESSION
    }

    def "when participant makes new reservation should add the session to schedule"() {
        when:
        participant.makeReservation(session)

        then:
        participant.hasReservation(session)
    }
}
