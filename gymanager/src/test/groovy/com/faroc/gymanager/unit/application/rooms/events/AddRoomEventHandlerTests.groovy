package com.faroc.gymanager.unit.application.rooms.events

import com.faroc.gymanager.sessionmanagement.application.rooms.events.AddRoomEventHandler
import com.faroc.gymanager.sessionmanagement.application.rooms.gateways.RoomsGateway
import com.faroc.gymanager.gymmanagement.domain.gyms.events.AddRoomEvent
import com.faroc.gymanager.sessionmanagement.domain.rooms.Room
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException
import com.faroc.gymanager.unit.domain.rooms.utils.RoomsTestsFactory
import spock.lang.Specification

class AddRoomEventHandlerTests extends Specification {
    AddRoomEvent event
    Room room
    RoomsGateway mockRoomsGateway

    AddRoomEventHandler sut

    def setup() {
        mockRoomsGateway = Mock(RoomsGateway)
        room = RoomsTestsFactory.create()
        event = new AddRoomEvent(room)

        sut = new AddRoomEventHandler(mockRoomsGateway)
    }

    def "when creating room fails should throw eventual consistency exception"() {
        given:
        mockRoomsGateway.create(room) >> { throw new RuntimeException() }

        when:
        sut.handle(event)

        then:
        thrown(EventualConsistencyException)
    }
}
