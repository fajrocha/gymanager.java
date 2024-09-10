package com.faroc.gymanager.gymmanagement.unit.application.gyms.events

import com.faroc.gymanager.gymmanagement.application.gyms.events.UnsubscribeGymsEventHandler
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.gymmanagement.domain.admins.events.UnsubscribeEvent
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException
import spock.lang.Specification

class UnsubscribeGymsEventHandlerTests extends Specification {
    UUID subscriptionId
    UnsubscribeEvent event
    GymsGateway mockGymsGateway

    UnsubscribeGymsEventHandler sut

    def setup() {
        subscriptionId = UUID.randomUUID()
        event = new UnsubscribeEvent(subscriptionId)
        mockGymsGateway = Mock(GymsGateway)

        sut = new UnsubscribeGymsEventHandler(mockGymsGateway)
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
