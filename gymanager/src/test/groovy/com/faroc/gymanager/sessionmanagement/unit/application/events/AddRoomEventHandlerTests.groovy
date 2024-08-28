package com.faroc.gymanager.sessionmanagement.unit.application.events

import com.faroc.gymanager.gymmanagement.domain.rooms.RoomGym
import com.faroc.gymanager.sessionmanagement.application.rooms.events.AddRoomEventHandler
import com.faroc.gymanager.sessionmanagement.application.rooms.gateways.RoomsGateway
import com.faroc.gymanager.gymmanagement.domain.gyms.events.AddRoomEvent
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException
import com.faroc.gymanager.gymmanagement.unit.domain.rooms.utils.RoomsGymTestsFactory
import com.faroc.gymanager.sessionmanagement.domain.rooms.Room
import spock.lang.Specification

class AddRoomEventHandlerTests extends Specification {
    AddRoomEvent event
    RoomGym room
    RoomsGateway mockRoomsGateway

    AddRoomEventHandler sut

    def setup() {
        mockRoomsGateway = Mock(RoomsGateway)
        room = RoomsGymTestsFactory.create()
        event = new AddRoomEvent(room)

        sut = new AddRoomEventHandler(mockRoomsGateway)
    }

    def "when creating room fails should throw eventual consistency exception"() {
        given:
        mockRoomsGateway.create(_ as Room) >> { throw new RuntimeException() }

        when:
        sut.handle(event)

        then:
        thrown(EventualConsistencyException)
    }
}
