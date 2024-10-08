package com.faroc.gymanager.sessionmanagement.unit.domain.sessions

import com.faroc.gymanager.sessionmanagement.domain.sessions.Session
import com.faroc.gymanager.sessionmanagement.domain.sessions.errors.SessionErrors
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation
import com.faroc.gymanager.sessionmanagement.domain.sessions.exceptions.CancellationTooCloseToSession
import com.faroc.gymanager.sessionmanagement.domain.sessions.exceptions.MaxParticipantsReachedException
import com.faroc.gymanager.common.domain.abstractions.InstantProvider
import com.faroc.gymanager.common.domain.exceptions.ConflictException
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot
import com.faroc.gymanager.sessionmanagement.unit.domain.sessions.utils.SessionsTestsFactory
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

class SessionsTests extends Specification {
    final MAX_PARTICIPANTS = 1
    
    SessionReservation reservation
    InstantProvider instantProvider

    Session session

    def setup() {
        def participantId = UUID.randomUUID()
        reservation = new SessionReservation(participantId)

        def startTime = Instant.now()
        def endTime = Instant.now().plus(1, ChronoUnit.HOURS)
        def timeRange = new TimeSlot(
                startTime,
                endTime
        )

        instantProvider = Mock(InstantProvider)

        session = SessionsTestsFactory.create(
                timeRange,
                MAX_PARTICIPANTS
        )
    }

    def "when participant makes reservation and session not full should add participant"() {
        when:
        session.makeReservation(reservation)

        then:
        session.hasReservation(reservation)
    }

    def "when participant makes reservation but session is full should throw max participants exception"() {
        given:
        def anotherReservation = new SessionReservation(UUID.randomUUID())
        session.makeReservation(reservation)

        when:
        session.makeReservation(anotherReservation)

        then:
        def ex = thrown(MaxParticipantsReachedException)
        ex.getDetail() == SessionErrors.MAX_PARTICIPANTS_REACHED
    }

    def "when participant makes reservation twice should throw conflict exception"() {
        given:
        final MAX_PARTICIPANTS = 2
        def session = SessionsTestsFactory.create(MAX_PARTICIPANTS)

        session.makeReservation(reservation)

        when:
        session.makeReservation(reservation)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == SessionErrors.CONFLICT_PARTICIPANT
    }

    def "when participant cancels reservation too close to session start should throw exception"() {
        given:
        def deltaHours = Session.MIN_CANCELLATION_HOURS - 12
        session.makeReservation(reservation)

        instantProvider.now() >> Instant.now().minus(deltaHours, ChronoUnit.HOURS)

        when:
        session.cancelReservation(reservation, instantProvider)

        then:
        def ex = thrown(CancellationTooCloseToSession)
        ex.getDetail() == SessionErrors.CANCELLATION_CLOSE_TO_START
    }

    def "when participant cancels reservation on time should cancel reservation"() {
        given:
        final DELTA_HOURS = Session.MIN_CANCELLATION_HOURS + 12
        session.makeReservation(reservation)

        instantProvider.now() >> Instant.now().minus(DELTA_HOURS, ChronoUnit.HOURS)

        when:
        session.cancelReservation(reservation, instantProvider)

        then:
        !session.hasReservation(reservation)
    }

    def "when participant cancels reservation but does not have a reservation should throw unexpected exception"() {
        given:
        final DELTA_HOURS = Session.MIN_CANCELLATION_HOURS + 12
        instantProvider.now() >> Instant.now().minus(DELTA_HOURS, ChronoUnit.HOURS)

        when:
        session.cancelReservation(reservation, instantProvider)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.PARTICIPANT_NOT_FOUND
    }
}
