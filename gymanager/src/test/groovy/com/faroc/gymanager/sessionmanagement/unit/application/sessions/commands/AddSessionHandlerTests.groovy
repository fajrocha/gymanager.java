package com.faroc.gymanager.sessionmanagement.unit.application.sessions.commands

import com.faroc.gymanager.sessionmanagement.application.rooms.gateways.RoomsGateway
import com.faroc.gymanager.sessionmanagement.application.sessions.commands.addsession.AddSessionCommand
import com.faroc.gymanager.sessionmanagement.application.sessions.commands.addsession.AddSessionHandler
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher
import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionCategoriesGateway
import com.faroc.gymanager.sessionmanagement.application.trainers.gateways.TrainersGateway

import com.faroc.gymanager.sessionmanagement.domain.rooms.Room

import com.faroc.gymanager.common.domain.exceptions.ConflictException
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot
import com.faroc.gymanager.sessionmanagement.domain.sessions.errors.SessionErrors
import com.faroc.gymanager.sessionmanagement.domain.trainers.Trainer
import com.faroc.gymanager.sessionmanagement.unit.domain.rooms.utils.RoomsTestsFactory
import com.faroc.gymanager.sessionmanagement.unit.domain.sessions.utils.SessionsTestsFactory
import com.faroc.gymanager.sessionmanagement.unit.domain.trainers.utils.TrainersTestsFactory
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

class AddSessionHandlerTests extends Specification {
    final String SESSION_NAME = "Amazing Session"
    final String SESSION_DESCRIPTION = "Amazing session indeed"
    final String SESSION_CATEGORY = "Pilates"
    final int SESSION_MAX_PARTICIPANTS = 12

    UUID roomId
    UUID trainerId
    Room room
    Trainer trainer
    AddSessionCommand command
    RoomsGateway mockRoomsGateway
    TrainersGateway mockTrainersGateway
    SessionCategoriesGateway mockSessionCategoriesRepository
    DomainEventsPublisher mockDomainEventsPublisher

    AddSessionHandler sut

    def setup() {
        def startTime = Instant.now()
        def endTime = Instant.now().plus(1 , ChronoUnit.HOURS)

        roomId = UUID.randomUUID()
        trainerId = UUID.randomUUID()

        room = RoomsTestsFactory.create(roomId)
        trainer = TrainersTestsFactory.create(trainerId)

        command = new AddSessionCommand(
                SESSION_NAME,
                SESSION_DESCRIPTION,
                SESSION_CATEGORY,
                SESSION_MAX_PARTICIPANTS,
                startTime,
                endTime,
                trainerId,
                roomId
        )
        mockRoomsGateway = Mock(RoomsGateway)
        mockTrainersGateway = Mock(TrainersGateway)
        mockSessionCategoriesRepository = Mock(SessionCategoriesGateway)
        mockDomainEventsPublisher = Mock(DomainEventsPublisher)

        sut = new AddSessionHandler(mockRoomsGateway, mockTrainersGateway, mockSessionCategoriesRepository, mockDomainEventsPublisher)
    }

    def "when adding session and room does not exist should throw unexpected exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.ADD_SESSION_ROOM_NOT_FOUND
        ex.getMessage() == SessionErrors.addSessionRoomNotFound(roomId)
    }

    def "when adding session and trainer does not exist should throw unexpected exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockTrainersGateway.findById(trainerId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.TRAINER_NOT_FOUND
        ex.getMessage() == SessionErrors.trainerNotFound(trainerId)
    }

    def "when adding session and trainer is not free should throw conflict exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        def timeSlot = TimeSlot.fromInstants(command.startTime(), command.endTime())
        def session = SessionsTestsFactory.create(timeSlot)
        trainer.makeReservation(session)
        mockTrainersGateway.findById(trainerId) >> Optional.of(trainer)

        when:
        sut.handle(command)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == SessionErrors.TRAINER_SCHEDULE_CONFLICT
        ex.getMessage() == SessionErrors.trainerScheduleConflict(trainerId, timeSlot)
    }

    def "when adding session and platform does not support the category should throw unexpected exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockTrainersGateway.findById(trainerId) >> Optional.of(trainer)
        mockSessionCategoriesRepository.existsByName(SESSION_CATEGORY) >> false

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.SESSION_CATEGORY_NOT_SUPPORTED
        ex.getMessage() == SessionErrors.sessionCategoryNotSupported()
    }

    def "when adding session and room update fails should rethrow exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockTrainersGateway.findById(trainerId) >> Optional.of(trainer)
        mockSessionCategoriesRepository.existsByName(SESSION_CATEGORY) >> true
        mockRoomsGateway.update(room) >> { throw new RuntimeException() }

        when:
        sut.handle(command)

        then:
        thrown(RuntimeException)
    }

    def "when adding session and all conditions met should add to room and trigger domain events"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockTrainersGateway.findById(trainerId) >> Optional.of(trainer)
        mockSessionCategoriesRepository.existsByName(SESSION_CATEGORY) >> true

        when:
        def session = sut.handle(command)

        then:
        room.hasSessionReservation(session)
        1 * mockRoomsGateway.update(room)
        1 * mockDomainEventsPublisher.publishEventsFromAggregate(room)

        session.getTrainerId() == trainerId
        session.getRoomId() == roomId
        session.getName() == SESSION_NAME
        session.getDescription() == SESSION_DESCRIPTION
        session.getCategory() == SESSION_CATEGORY
        session.getMaximumNumberParticipants() == SESSION_MAX_PARTICIPANTS
    }
}
