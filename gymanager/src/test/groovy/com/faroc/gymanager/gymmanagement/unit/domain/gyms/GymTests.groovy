package com.faroc.gymanager.gymmanagement.unit.domain.gyms

import com.faroc.gymanager.gymmanagement.domain.gyms.Gym
import com.faroc.gymanager.gymmanagement.domain.gyms.errors.GymsErrors
import com.faroc.gymanager.gymmanagement.domain.gyms.exceptions.MaxRoomsReachedException
import com.faroc.gymanager.common.domain.exceptions.ConflictException
import com.faroc.gymanager.gymmanagement.domain.rooms.RoomGym
import com.faroc.gymanager.gymmanagement.unit.domain.rooms.utils.RoomsGymTestsFactory
import com.faroc.gymanager.sessionmanagement.domain.trainers.Trainer
import com.faroc.gymanager.gymmanagement.unit.domain.gyms.utils.GymsTestsFactory
import com.faroc.gymanager.sessionmanagement.unit.domain.trainers.utils.TrainersTestsFactory
import spock.lang.Specification

class GymTests extends Specification {
    final int GYM_MAX_ROOMS_DEFAULT = 1

    Trainer trainer
    RoomGym room

    Gym gym
    def setup() {
        trainer = TrainersTestsFactory.create()
        room = RoomsGymTestsFactory.create()
        gym = GymsTestsFactory.create(GYM_MAX_ROOMS_DEFAULT)
    }

    def "when adding room and maximum rooms has been reached should throw exception"() {
        given:
        def anotherRoom = RoomsGymTestsFactory.create()
        gym.addRoom(room)

        when:
        gym.addRoom(anotherRoom)

        then:
        def ex = thrown(MaxRoomsReachedException)
        ex.getDetail() == GymsErrors.MAX_ROOMS_REACHED
    }

    def "when adding already existing room should throw conflict exception"() {
        given:
        gym.addRoom(room)

        when:
        gym.addRoom(room)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == GymsErrors.CONFLICT_ROOM
    }

    def "when adding room should register room"() {
        when:
        gym.addRoom(room)

        then:
        gym.hasRoom(room)
        gym.getRoomIds().size() == 1
    }

    def "when adding trainer already assigned to gym should throw conflict exception"() {
        given:
        def trainedId = UUID.randomUUID()
        gym.addTrainer(trainedId)

        when:
        gym.addTrainer(trainedId)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == GymsErrors.CONFLICT_TRAINER
    }

    def "when adding trainer should assign trainer to gym"() {
        given:
        def trainedId = UUID.randomUUID()

        when:
        gym.addTrainer(trainedId)

        then:
        gym.hasTrainer(trainedId)
        gym.getTrainerIds().size() == 1
    }
}
