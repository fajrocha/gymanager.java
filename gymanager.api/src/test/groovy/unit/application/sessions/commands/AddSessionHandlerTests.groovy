package unit.application.sessions.commands

import com.faroc.gymanager.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.application.rooms.gateways.RoomsGateway
import com.faroc.gymanager.application.sessions.commands.addsession.AddSessionCommand
import com.faroc.gymanager.application.sessions.commands.addsession.AddSessionHandler
import com.faroc.gymanager.application.shared.abstractions.DomainEventsPublisher
import com.faroc.gymanager.application.trainers.gateways.TrainersGateway
import com.faroc.gymanager.domain.gyms.Gym
import com.faroc.gymanager.domain.rooms.Room
import com.faroc.gymanager.domain.sessions.errors.SessionErrors
import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot
import com.faroc.gymanager.domain.trainers.Trainer
import spock.lang.Specification
import unit.domain.gyms.utils.GymsTestsFactory
import unit.domain.rooms.utils.RoomsTestsFactory
import unit.domain.sessions.utils.SessionsTestsFactory
import unit.domain.trainers.utils.TrainersTestsFactory

import java.time.Instant
import java.time.temporal.ChronoUnit

class AddSessionHandlerTests extends Specification {
    final String SESSION_NAME = "Amazing Session"
    final String SESSION_DESCRIPTION = "Amazing session indeed"
    final String SESSION_CATEGORY = "Pilates"
    final int SESSION_MAX_PARTICIPANTS = 12

    UUID roomId
    UUID trainerId
    UUID gymId
    Room room
    Trainer trainer
    Gym gym
    AddSessionCommand command
    RoomsGateway mockRoomsGateway
    TrainersGateway mockTrainersGateway
    GymsGateway mockGymsGateway
    DomainEventsPublisher mockDomainEventsPublisher

    AddSessionHandler sut

    def setup() {
        def startTime = Instant.now()
        def endTime = Instant.now().plus(1 , ChronoUnit.HOURS)

        roomId = UUID.randomUUID()
        trainerId = UUID.randomUUID()
        gymId = UUID.randomUUID()

        room = RoomsTestsFactory.create(roomId, gymId)
        trainer = TrainersTestsFactory.create(trainerId)
        gym = GymsTestsFactory.create(gymId, SESSION_CATEGORY)

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
        mockGymsGateway = Mock(GymsGateway)
        mockDomainEventsPublisher = Mock(DomainEventsPublisher)

        sut = new AddSessionHandler(mockRoomsGateway, mockTrainersGateway, mockGymsGateway, mockDomainEventsPublisher)
    }

    def "when adding session and room does not exist should throw unexpected exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.ROOM_NOT_FOUND
        ex.getMessage() == SessionErrors.roomNotFound(roomId)
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

    def "when adding session and gym does not exist should throw unexpected exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockTrainersGateway.findById(trainerId) >> Optional.of(trainer)
        mockGymsGateway.findById(gymId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.GYM_NOT_FOUND
        ex.getMessage() == SessionErrors.gymNotFound(gymId)
    }

    def "when adding session and gym does not offer the category exist should throw unexpected exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockTrainersGateway.findById(trainerId) >> Optional.of(trainer)
        def gym = GymsTestsFactory.create(gymId)
        mockGymsGateway.findById(gymId) >> Optional.of(gym)

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionErrors.SESSION_CATEGORY_NOT_FOUND
        ex.getMessage() == SessionErrors.sessionCategoryNotFound(gymId)
    }

    def "when adding session and room update fails should rethrow exception"() {
        given:
        mockRoomsGateway.findById(roomId) >> Optional.of(room)
        mockTrainersGateway.findById(trainerId) >> Optional.of(trainer)
        mockGymsGateway.findById(gymId) >> Optional.of(gym)
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
        mockGymsGateway.findById(gymId) >> Optional.of(gym)

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
