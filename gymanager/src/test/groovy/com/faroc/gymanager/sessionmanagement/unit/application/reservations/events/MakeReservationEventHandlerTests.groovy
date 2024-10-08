package com.faroc.gymanager.sessionmanagement.unit.application.reservations.events

import com.faroc.gymanager.sessionmanagement.application.reservations.events.MakeReservationEventHandler
import com.faroc.gymanager.sessionmanagement.application.reservations.gateways.ReservationsGateway
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation
import com.faroc.gymanager.sessionmanagement.domain.sessions.events.MakeReservationEvent
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException
import com.faroc.gymanager.sessionmanagement.unit.application.reservations.utils.SessionReservationTestsFactory
import com.faroc.gymanager.sessionmanagement.unit.domain.sessions.utils.SessionsTestsFactory
import spock.lang.Specification

class MakeReservationEventHandlerTests extends Specification {
    Session session
    SessionReservation sessionReservation
    MakeReservationEvent event

    ReservationsGateway mockReservationsGateway

    MakeReservationEventHandler sut

    def setup() {
        session = SessionsTestsFactory.create()
        sessionReservation = SessionReservationTestsFactory.create()
        event = new MakeReservationEvent(session, sessionReservation)
        mockReservationsGateway = Mock(ReservationsGateway)
        
        sut = new MakeReservationEventHandler(mockReservationsGateway)
    }
    

    def "when creating reservation fails should throw eventual consistency exception"() {
        given:
        mockReservationsGateway.create(sessionReservation, session.getId()) >> { throw new RuntimeException() }

        when:
        sut.handle(event)

        then:
        thrown(EventualConsistencyException)
    }
}
