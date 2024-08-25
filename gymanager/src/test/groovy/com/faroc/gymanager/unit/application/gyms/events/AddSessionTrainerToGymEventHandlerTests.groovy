package com.faroc.gymanager.unit.application.gyms.events

import com.faroc.gymanager.gymmanagement.application.gyms.events.AddSessionTrainerToGymEventHandler
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym
import com.faroc.gymanager.sessionmanagement.domain.rooms.Room
import com.faroc.gymanager.sessionmanagement.domain.rooms.events.SessionReservationEvent
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException
import com.faroc.gymanager.unit.domain.gyms.utils.GymsTestsFactory
import com.faroc.gymanager.unit.domain.rooms.utils.RoomsTestsFactory
import com.faroc.gymanager.unit.domain.sessions.utils.SessionsTestsFactory
import spock.lang.Specification

class AddSessionTrainerToGymEventHandlerTests extends Specification {

    Room room
    Session session
    Gym gym
    SessionReservationEvent event
    GymsGateway mockGymsGateway

    AddSessionTrainerToGymEventHandler sut

    def setup() {
        room = RoomsTestsFactory.create()
        session = SessionsTestsFactory.create()
        gym = GymsTestsFactory.create(room.getGymId())
        event = new SessionReservationEvent(room, session)
        mockGymsGateway = Mock(GymsGateway)

        sut = new AddSessionTrainerToGymEventHandler(mockGymsGateway)
    }

    def "when gym does not exist should throw eventual consistency exception"() {
        given:
        def gymId = event.room().getGymId()
        def trainerId = event.session().getTrainerId()
        mockGymsGateway.findById(gymId) >> Optional.empty()

        when:
        sut.handle(event)

        then:
        def ex = thrown(EventualConsistencyException)
        ex.message == SessionReservationEvent.gymNotFound(gymId, trainerId)
    }

    def "when gym update fails should throw eventual consistency exception"() {
        given:
        mockGymsGateway.findById(gym.getId()) >> Optional.of(gym)
        mockGymsGateway.update(gym) >> { throw new RuntimeException() }

        when:
        sut.handle(event)

        then:
        thrown(EventualConsistencyException)
    }

    def "when gym update succeeds should add trainer and update it"() {
        given:
        mockGymsGateway.findById(gym.getId()) >> Optional.of(gym)

        when:
        sut.handle(event)

        then:
        gym.trainerIds.contains(session.getTrainerId())
    }
}
