package unit.infrastructure.rooms.mappers

import com.faroc.gymanager.domain.rooms.Room
import com.faroc.gymanager.domain.sessions.Session
import com.faroc.gymanager.infrastructure.rooms.mappers.RoomMappers
import spock.lang.Specification
import unit.domain.rooms.utils.RoomsFactory
import unit.domain.sessions.utils.SessionsFactory

class RoomMappersTests extends Specification {
    Room room
    Session session

    def setup() {
        room = RoomsFactory.create()
        session = SessionsFactory.create()
    }

    def "when mapping to record should return valid record"() {
        when:
        room.makeReservation(session)
        
        var roomRecord = RoomMappers.toRecordCreate(room)

        then:
        roomRecord.getId() == room.getId()
    }
}
