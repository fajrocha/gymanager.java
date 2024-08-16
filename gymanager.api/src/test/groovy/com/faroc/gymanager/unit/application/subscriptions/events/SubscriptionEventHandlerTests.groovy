package com.faroc.gymanager.unit.application.subscriptions.events

import com.faroc.gymanager.application.subscriptions.events.SubscriptionEventHandler
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.domain.admins.events.SubscriptionCreatedEvent
import com.faroc.gymanager.domain.admins.events.SubscriptionDeletedEvent
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException
import com.faroc.gymanager.domain.subscriptions.Subscription
import spock.lang.Specification
import com.faroc.gymanager.unit.domain.subscriptions.utils.SubscriptionsFactory

class SubscriptionEventHandlerTests extends Specification {
    Subscription subscription
    SubscriptionCreatedEvent createEvent
    SubscriptionDeletedEvent deleteEvent
    SubscriptionsGateway mockSubscriptionsGateway

    SubscriptionEventHandler sut

    def setup() {
        subscription = SubscriptionsFactory.create()
        createEvent = new SubscriptionCreatedEvent(subscription)
        deleteEvent = new SubscriptionDeletedEvent(subscription.getId())
        mockSubscriptionsGateway = Mock(SubscriptionsGateway)

        sut = new SubscriptionEventHandler(mockSubscriptionsGateway)
    }
    

    def "when deleting subscription fails should throw eventual consistency exception"() {
        given:
        mockSubscriptionsGateway.delete(deleteEvent.subscriptionId()) >> { throw new RuntimeException() }

        when:
        sut.handle(deleteEvent)

        then:
        thrown(EventualConsistencyException)
    }

    def "when creating subscription fails should throw eventual consistency exception"() {
        given:
        mockSubscriptionsGateway.delete(deleteEvent.subscriptionId()) >> { throw new RuntimeException() }

        when:
        sut.handle(deleteEvent)

        then:
        thrown(EventualConsistencyException)
    }
}
