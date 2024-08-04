package unit.application.gyms.events

import com.faroc.gymanager.application.gyms.events.DeleteSubscriptionGymsEventHandler
import com.faroc.gymanager.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.domain.admins.events.SubscriptionDeletedEvent
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException
import spock.lang.Specification

class DeleteSubscriptionGymsEventHandlerTests extends Specification {
    UUID subscriptionId
    SubscriptionDeletedEvent event
    GymsGateway mockGymsGateway

    DeleteSubscriptionGymsEventHandler sut

    def setup() {
        subscriptionId = UUID.randomUUID()
        event = new SubscriptionDeletedEvent(subscriptionId)
        mockGymsGateway = Mock(GymsGateway)

        sut = new DeleteSubscriptionGymsEventHandler(mockGymsGateway)
    }


    def "when deleting gyms by subscription fails should throw eventual consistency exception"() {
        given:
        mockGymsGateway.deleteBySubscription(subscriptionId) >> { throw new RuntimeException() }

        when:
        sut.handle(event)

        then:
        thrown(EventualConsistencyException)
    }

    def "when deleting gyms by subscription succeeds should delete gyms"() {
        when:
        sut.handle(event)

        then:
        1 * mockGymsGateway.deleteBySubscription(event.subscriptionId())
    }
}
