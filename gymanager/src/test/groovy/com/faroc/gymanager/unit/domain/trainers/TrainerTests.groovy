package com.faroc.gymanager.unit.domain.trainers

import com.faroc.gymanager.sessionmanagement.domain.sessions.Session
import com.faroc.gymanager.common.domain.exceptions.ConflictException
import com.faroc.gymanager.sessionmanagement.domain.trainers.Trainer
import com.faroc.gymanager.sessionmanagement.domain.trainers.errors.TrainerErrors
import com.faroc.gymanager.unit.domain.sessions.utils.SessionsTestsFactory
import com.faroc.gymanager.unit.domain.trainers.utils.TrainersTestsFactory
import spock.lang.Specification

class TrainerTests extends Specification {

    final MAX_SESSION_PARTICIPANTS = 1
    Session session

    Trainer trainer

    def setup() {
        session = SessionsTestsFactory.create(MAX_SESSION_PARTICIPANTS)

        trainer = TrainersTestsFactory.create()
    }

    def "when session reservation already exists on trainer should throw conflict exception"() {
        given:
        trainer.makeReservation(session)

        when:
        trainer.makeReservation(session)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == TrainerErrors.CONFLICT_SESSION
    }

    def "when trainer makes new reservation should add the session to schedule"() {
        when:
        trainer.makeReservation(session)

        then:
        trainer.hasSessionReservation(session)
    }
}
