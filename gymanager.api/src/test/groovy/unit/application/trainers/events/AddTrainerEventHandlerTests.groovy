package unit.application.trainers.events


import com.faroc.gymanager.application.trainers.events.AddTrainerEventHandler
import com.faroc.gymanager.application.trainers.gateways.TrainersGateway
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException
import com.faroc.gymanager.domain.trainers.Trainer
import com.faroc.gymanager.domain.users.events.AddTrainerEvent
import spock.lang.Specification

class AddTrainerEventHandlerTests extends Specification {

    UUID userId
    UUID trainerId
    AddTrainerEvent event
    AddTrainerEventHandler sut
    TrainersGateway mockTrainersGateway

    def setup() {
        userId = UUID.randomUUID()
        trainerId = UUID.randomUUID()
        event = new AddTrainerEvent(trainerId, userId)
        
        mockTrainersGateway = Mock(TrainersGateway)

        sut = new AddTrainerEventHandler(mockTrainersGateway)
    }

    def "when add trainer event is triggered should create trainer"() {
        when:
        sut.handle(event)

        then:
        1 * mockTrainersGateway.create(_ as Trainer)
    }

    def "when add trainer event is triggered and creating trainer fails should throw eventual consistency exception"() {
        given:
        mockTrainersGateway.create(_ as Trainer) >> { throw new RuntimeException() }

        when:
        sut.handle(event)

        then:
        thrown(EventualConsistencyException)
    }
}
