package unit.domain.trainers

import com.faroc.gymanager.domain.sessions.Session
import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import com.faroc.gymanager.domain.trainers.Trainer
import com.faroc.gymanager.domain.trainers.errors.TrainerErrors
import spock.lang.Specification
import unit.domain.sessions.utils.SessionsTestFactory
import unit.domain.trainers.utils.TrainersFactory

class TrainerTests extends Specification {

    final MAX_SESSION_PARTICIPANTS = 1
    Session session

    Trainer trainer

    def setup() {
        session = SessionsTestFactory.create(MAX_SESSION_PARTICIPANTS)

        trainer = TrainersFactory.create()
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
