package com.faroc.gymanager.sessionmanagement.unit.domain.participants

import com.faroc.gymanager.sessionmanagement.domain.participants.Participant
import com.faroc.gymanager.sessionmanagement.domain.participants.errors.ParticipantErrors
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session
import com.faroc.gymanager.common.domain.exceptions.ConflictException
import com.faroc.gymanager.sessionmanagement.unit.domain.participants.utils.ParticipantFactory
import com.faroc.gymanager.sessionmanagement.unit.domain.sessions.utils.SessionsTestsFactory
import spock.lang.Specification

class ParticipantTests extends Specification {

    final MAX_SESSION_PARTICIPANTS = 1
    Participant participant

    Session session

    def setup() {
        session = SessionsTestsFactory.create(MAX_SESSION_PARTICIPANTS)
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
