package unit.application.sessions.queries

import com.faroc.gymanager.application.rooms.gateways.RoomsGateway
import com.faroc.gymanager.application.sessions.gateways.SessionsGateway
import com.faroc.gymanager.application.sessions.queries.getsession.FetchSessionHandler
import com.faroc.gymanager.application.sessions.queries.getsession.FetchSessionQuery
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException
import com.faroc.gymanager.domain.rooms.Room
import com.faroc.gymanager.domain.sessions.Session
import com.faroc.gymanager.domain.sessions.SessionErrors
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException
import spock.lang.Specification
import unit.domain.rooms.utils.RoomsTestFactory
import unit.domain.sessions.utils.SessionsTestsFactory

class FetchSessionHandlerTests extends Specification {
    UUID roomId
    UUID sessionId
    Room room
    Session session
    FetchSessionQuery query
    RoomsGateway mockRoomsGateway
    SessionsGateway mockSessionsGateway

    FetchSessionHandler sut

    def setup() {
        roomId = UUID.randomUUID()
        sessionId = UUID.randomUUID()
        room = RoomsTestFactory.create(roomId)
        session = SessionsTestsFactory.create(sessionId)

        query = new FetchSessionQuery(roomId, sessionId)
        mockRoomsGateway = Mock(RoomsGateway)
        mockSessionsGateway = Mock(SessionsGateway)

        sut = new FetchSessionHandler(mockRoomsGateway, mockSessionsGateway)
    }

    def "when fetching session and room is not found should throw unexpected exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.empty()

        when:
        sut.handle(query)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.ROOM_NOT_FOUND
        ex.getMessage() == SessionErrors.roomNotFound(roomId, sessionId)
    }

    def "when fetching session and session is not found should throw not found exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockSessionsGateway.findById(sessionId) >> Optional.empty()

        when:
        sut.handle(query)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.getDetail() == SessionErrors.NOT_FOUND
        ex.getMessage() == SessionErrors.notFound(sessionId)
    }

    def "when fetching session and room does not have session should throw unexpected exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockSessionsGateway.findById(sessionId) >> Optional.of(session)

        when:
        sut.handle(query)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.NOT_FOUND_ON_ROOM
        ex.getMessage() == SessionErrors.notFoundOnRoom(sessionId, roomId)
    }

    def "when fetching session should return session"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockSessionsGateway.findById(sessionId) >> Optional.of(session)
        room.makeReservation(session)

        when:
        def session = sut.handle(query)

        then:
        session == this.session
    }
}
