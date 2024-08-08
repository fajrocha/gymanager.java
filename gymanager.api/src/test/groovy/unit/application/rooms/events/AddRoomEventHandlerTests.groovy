package unit.application.rooms.events

import com.faroc.gymanager.application.rooms.events.AddRoomEventHandler
import com.faroc.gymanager.application.rooms.gateways.RoomsGateway
import com.faroc.gymanager.domain.gyms.events.AddRoomEvent
import com.faroc.gymanager.domain.rooms.Room
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException
import spock.lang.Specification
import unit.domain.rooms.utils.RoomsTestsFactory

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
