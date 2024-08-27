package com.faroc.gymanager.sessionmanagement.unit.domain.rooms

import com.faroc.gymanager.sessionmanagement.domain.rooms.Room
import com.faroc.gymanager.sessionmanagement.domain.rooms.errors.RoomErrors
import com.faroc.gymanager.sessionmanagement.domain.rooms.exceptions.MaxSessionsReachedException
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session
import com.faroc.gymanager.common.domain.exceptions.ConflictException
import com.faroc.gymanager.sessionmanagement.unit.domain.rooms.utils.RoomsTestsFactory
import com.faroc.gymanager.sessionmanagement.unit.domain.sessions.utils.SessionsTestsFactory
import spock.lang.Specification

class RoomTests extends Specification {

    final MAX_DAILY_SESSIONS = 1
    Session session
    final MAX_SESSION_PARTICIPANTS = 1

    Room room

    def setup() {
        session = SessionsTestsFactory.create(MAX_SESSION_PARTICIPANTS)
        room = RoomsTestsFactory.create(MAX_DAILY_SESSIONS)
    }

    def "when session reservation already made on room should throw conflict exception"() {
        given:
        room.makeReservation(session)

        when:
        room.makeReservation(session)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == RoomErrors.CONFLICT_SESSION
    }

    def "when max daily sessions reached for room should throw max session exception"() {
        given:
        def anotherSession = SessionsTestsFactory.create(MAX_DAILY_SESSIONS)
        room.makeReservation(session)

        when:
        room.makeReservation(anotherSession)

        then:
        def ex = thrown(MaxSessionsReachedException)
        ex.getDetail() == RoomErrors.MAX_SESSIONS_REACHED
    }

    def "when new reservation is made to room should add the session to schedule"() {
        when:
        room.makeReservation(session)

        then:
        room.hasSessionReservation(session)
    }
}
