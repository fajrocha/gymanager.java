package com.faroc.gymanager.unit.application.participants.events

import com.faroc.gymanager.application.participants.events.MakeReservationParticipantEventHandler
import com.faroc.gymanager.application.participants.gateways.ParticipantsGateway
import com.faroc.gymanager.domain.participants.Participant
import com.faroc.gymanager.domain.sessions.Session
import com.faroc.gymanager.domain.sessions.SessionReservation
import com.faroc.gymanager.domain.sessions.events.MakeReservationEvent
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException
import com.faroc.gymanager.unit.application.participants.utils.ParticipantsTestsFactory
import com.faroc.gymanager.unit.application.reservations.utils.SessionReservationTestsFactory
import com.faroc.gymanager.unit.domain.sessions.utils.SessionsTestsFactory
import spock.lang.Specification

class MakeReservationParticipantEventHandlerTests extends Specification {

    Session session
    SessionReservation sessionReservation
    UUID participantId
    MakeReservationEvent event
    Participant participant
    ParticipantsGateway mockParticipantsGateway

    MakeReservationParticipantEventHandler sut

    def setup() {

        session = SessionsTestsFactory.create()
        sessionReservation = SessionReservationTestsFactory.create()
        participantId = sessionReservation.getParticipantId()
        participant = ParticipantsTestsFactory.create(participantId)
        event = new MakeReservationEvent(session, sessionReservation)
        mockParticipantsGateway = Mock(ParticipantsGateway)
        
        sut = new MakeReservationParticipantEventHandler(mockParticipantsGateway)
    }

    def "when participant is not found should throw eventual consistency exception"() {
        given:
        mockParticipantsGateway.findById(participantId) >> Optional.empty()

        when:
        sut.handle(event)

        then:
        def ex = thrown(EventualConsistencyException)
        ex.getMessage() == MakeReservationEvent.participantNotFound(
                participantId,
                sessionReservation.getId(),
                session.getId())
    }

    def "when participant update fails should throw eventual consistency exception"() {
        given:
        mockParticipantsGateway.findById(participantId) >> Optional.of(participant)
        mockParticipantsGateway.update(participant) >> { throw new RuntimeException() }

        when:
        sut.handle(event)

        then:
        thrown(EventualConsistencyException)
    }
}
