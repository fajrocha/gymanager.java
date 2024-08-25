package com.faroc.gymanager.unit.application.rooms.commands

import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.gymmanagement.application.rooms.commands.addroom.AddRoomCommand
import com.faroc.gymanager.gymmanagement.application.rooms.commands.addroom.AddRoomHandler
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym
import com.faroc.gymanager.sessionmanagement.domain.rooms.errors.RoomErrors
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription
import com.faroc.gymanager.unit.application.subscriptions.utils.SubscriptionsTestsFactory
import com.faroc.gymanager.unit.domain.gyms.utils.GymsTestsFactory
import spock.lang.Specification

class AddRoomHandlerTests extends Specification {
    final String GYM_NAME = "Best Gym"
    AddRoomCommand command
    UUID gymId
    Gym gym
    UUID subscriptionId
    Subscription subscription
    GymsGateway mockGymsGateway
    SubscriptionsGateway mockSubscriptionsGateway
    DomainEventsPublisher mockDomainEventsPublisher

    AddRoomHandler sut

    def setup() {
        gymId = UUID.randomUUID()
        command = new AddRoomCommand(gymId, GYM_NAME)
        gym = GymsTestsFactory.create(gymId)
        subscriptionId = gym.getSubscriptionId()
        subscription = SubscriptionsTestsFactory.create(subscriptionId)
        
        mockGymsGateway = Mock(GymsGateway)
        mockSubscriptionsGateway = Mock(SubscriptionsGateway)
        mockDomainEventsPublisher = Mock(DomainEventsPublisher)

        sut = new AddRoomHandler(mockGymsGateway, mockSubscriptionsGateway, mockDomainEventsPublisher)
    }

    def "when adding room and gym does not exist should return unexpected exception"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == RoomErrors.GYM_NOT_FOUND
        ex.getMessage() == RoomErrors.gymNotFound(gymId)
    }

    def "when adding room and subscription does not exist should return unexpected exception"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.of(gym)
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == RoomErrors.SUBSCRIPTION_NOT_FOUND
        ex.getMessage() == RoomErrors.subscriptionNotFound(subscriptionId)
    }

    def "when adding room and updating the gym throws exception should rethrow exception"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.of(gym)
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.of(subscription)
        mockGymsGateway.update(gym) >> { throw new RuntimeException() }

        when:
        sut.handle(command)

        then:
        thrown(RuntimeException)
    }

    def "when adding room should add room to gym and trigger the domain events"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.of(gym)
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.of(subscription)

        when:
        def room = sut.handle(command)

        then:
        room.getGymId() == gym.getId()
        room.getName() == GYM_NAME
        room.getMaxDailySessions() == subscription.getMaxDailySessions()

        gym.hasRoom(room)
        1 * mockGymsGateway.update(gym)
        1 * mockDomainEventsPublisher.publishEventsFromAggregate(gym)
    }
}
