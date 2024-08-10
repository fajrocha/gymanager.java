package com.faroc.gymanager.unit.domain.gyms

import com.faroc.gymanager.domain.gyms.Gym
import com.faroc.gymanager.domain.gyms.errors.GymsErrors
import com.faroc.gymanager.domain.gyms.exceptions.MaxRoomsReachedException
import com.faroc.gymanager.domain.rooms.Room
import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import com.faroc.gymanager.domain.trainers.Trainer
import com.faroc.gymanager.unit.domain.gyms.utils.GymsTestsFactory
import com.faroc.gymanager.unit.domain.rooms.utils.RoomsTestsFactory
import com.faroc.gymanager.unit.domain.trainers.utils.TrainersTestsFactory
import spock.lang.Specification

class GymTests extends Specification {
    final int GYM_MAX_ROOMS_DEFAULT = 1

    Trainer trainer
    Room room

    Gym gym
    def setup() {
        trainer = TrainersTestsFactory.create()
        room = RoomsTestsFactory.create()
        gym = GymsTestsFactory.create(GYM_MAX_ROOMS_DEFAULT)
    }

    def "when adding room and maximum rooms has been reached should throw exception"() {
        given:
        def anotherRoom = RoomsTestsFactory.create()
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
        gym.addTrainer(trainer)

        when:
        gym.addTrainer(trainer)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == GymsErrors.CONFLICT_TRAINER
    }

    def "when adding trainer should assign trainer to gym"() {
        when:
        gym.addTrainer(trainer)

        then:
        gym.hasTrainer(trainer)
        gym.getTrainerIds().size() == 1
    }
}
