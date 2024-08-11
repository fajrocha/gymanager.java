package com.faroc.gymanager.unit.application.reservations.events

import com.faroc.gymanager.application.reservations.events.MakeReservationEventHandler
import com.faroc.gymanager.application.reservations.gateways.ReservationsGateway
import com.faroc.gymanager.domain.sessions.Session
import com.faroc.gymanager.domain.sessions.SessionReservation
import com.faroc.gymanager.domain.sessions.events.MakeReservationEvent
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException
import com.faroc.gymanager.unit.application.reservations.utils.SessionReservationTestsFactory
import com.faroc.gymanager.unit.domain.sessions.utils.SessionsTestsFactory
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
