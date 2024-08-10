package com.faroc.gymanager.unit.application.reservations.commands

import com.faroc.gymanager.application.participants.gateways.ParticipantsGateway
import com.faroc.gymanager.application.reservations.commands.addreservation.MakeSessionReservationCommand
import com.faroc.gymanager.application.reservations.commands.addreservation.MakeSessionReservationHandler
import com.faroc.gymanager.application.sessions.gateways.SessionsGateway
import com.faroc.gymanager.application.shared.abstractions.DomainEventsPublisher
import com.faroc.gymanager.domain.participants.Participant
import com.faroc.gymanager.domain.sessions.Session
import com.faroc.gymanager.domain.sessions.SessionErrors
import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException
import com.faroc.gymanager.unit.application.participants.utils.ParticipantsTestsFactory
import spock.lang.Specification
import unit.domain.sessions.utils.SessionsTestsFactory

class MakeSessionReservationHandlerTests extends Specification {
    SessionsGateway mockSessionsGateway
    ParticipantsGateway mockParticipantsGateway
    DomainEventsPublisher mockDomainEventsPublisher
    UUID sessionId
    UUID participantId
    Session session
    Participant participant
    MakeSessionReservationCommand command

    MakeSessionReservationHandler sut

    def setup() {
        mockSessionsGateway = Mock(SessionsGateway)
        mockParticipantsGateway = Mock(ParticipantsGateway)
        mockDomainEventsPublisher = Mock(DomainEventsPublisher)

        sessionId = UUID.randomUUID()
        participantId = UUID.randomUUID()

        session = SessionsTestsFactory.create(sessionId)
        participant = ParticipantsTestsFactory.create(participantId)
        
        command = new MakeSessionReservationCommand(sessionId, participantId)

        sut = new MakeSessionReservationHandler(mockSessionsGateway, mockParticipantsGateway, mockDomainEventsPublisher)
    }

    def "when making session reservation and session is not found should return unexpected exception"() {
        given:
        mockSessionsGateway.findById(sessionId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.NOT_FOUND_FOR_RESERVATION
        ex.getMessage() == SessionErrors.notFoundForReservation(sessionId)
    }

    def "when making session reservation and participant is not found should return unexpected exception"() {
        given:
        mockSessionsGateway.findById(sessionId) >> Optional.of(session)
        mockParticipantsGateway.findById(participantId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.PARTICIPANT_NOT_FOUND_TO_MAKE_RESERVATION
        ex.getMessage() == SessionErrors.participantNotFoundToMakeReservation(sessionId, participantId)
    }

    def "when making session reservation and participant is not free found should return conflict exception"() {
        given:
        def participant = ParticipantsTestsFactory.create(participantId)
        participant.makeReservation(session)

        mockSessionsGateway.findById(sessionId) >> Optional.of(session)
        mockParticipantsGateway.findById(participantId) >> Optional.of(participant)

        when:
        sut.handle(command)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == SessionErrors.PARTICIPANT_NOT_FREE_TO_MAKE_RESERVATION
        ex.getMessage() == SessionErrors.participantNotFreeToMakeReservation(sessionId, participantId)
    }

    def "when making session reservation and participant is free should make reservation on session and publish event"() {
        given:
        mockSessionsGateway.findById(sessionId) >> Optional.of(session)
        mockParticipantsGateway.findById(participantId) >> Optional.of(participant)

        when:
        def reservation = sut.handle(command)

        then:
        reservation.getParticipantId() == participantId
        session.hasReservation(reservation)
        1 * mockDomainEventsPublisher.publishEventsFromAggregate(session)
    }
}
