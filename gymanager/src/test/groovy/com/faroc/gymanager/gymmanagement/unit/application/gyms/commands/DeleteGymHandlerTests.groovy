package com.faroc.gymanager.gymmanagement.unit.application.gyms.commands

import com.faroc.gymanager.gymmanagement.application.gyms.commands.deletegym.DeleteGymCommand
import com.faroc.gymanager.gymmanagement.application.gyms.commands.deletegym.DeleteGymHandler
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym
import com.faroc.gymanager.gymmanagement.domain.gyms.errors.GymsErrors
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType
import com.faroc.gymanager.gymmanagement.domain.subscriptions.errors.SubscriptionErrors
import com.faroc.gymanager.gymmanagement.unit.application.subscriptions.utils.SubscriptionsTestsFactory
import com.faroc.gymanager.gymmanagement.unit.domain.gyms.utils.GymsTestsFactory
import net.datafaker.Faker
import spock.lang.Specification

class DeleteGymHandlerTests extends Specification {
    Faker faker
    DeleteGymHandler sut
    UUID subscriptionId
    String gymName
    UUID gymId
    UUID anotherGymId
    Gym gym
    Subscription subscription
    DeleteGymCommand command

    GymsGateway mockGymsGateway
    SubscriptionsGateway mockSubscriptionsGateway

    def setup() {
        faker = new Faker()
        subscriptionId = UUID.randomUUID()
        gymName = faker.company().name()
        gymId = UUID.randomUUID()
        anotherGymId = UUID.randomUUID()
        gym = GymsTestsFactory.create(gymId, subscriptionId)
        subscription = SubscriptionsTestsFactory.create(subscriptionId, List.of(gymId))

        command = new DeleteGymCommand(gymId, subscriptionId)

        mockSubscriptionsGateway = Mock(SubscriptionsGateway)
        mockGymsGateway = Mock(GymsGateway)

        sut = new DeleteGymHandler(mockSubscriptionsGateway, mockGymsGateway)
    }
    
    def "when gym not found throw not found exception"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.getDetail() == GymsErrors.NOT_FOUND
    }

    def "when subscription not found throw not found exception"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.of(gym)
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.getDetail() == SubscriptionErrors.NOT_FOUND
    }

    def "when subscription does not have gym to delete should throw unexpected exception"() {
        given:
        def subscription = SubscriptionsTestsFactory.create(subscriptionId, List.of(anotherGymId))
        mockGymsGateway.findById(gymId) >> Optional.of(gym)
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.of(subscription)

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == GymsErrors.NOT_FOUND_ON_SUBSCRIPTION
    }

    def "when called should delete gym and remove it from subscription gyms"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.of(gym)
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.of(subscription)

        when:
        sut.handle(command)

        then:
        !subscription.hasGym(gymId)
        1 * mockSubscriptionsGateway.update(subscription)
        1 * mockGymsGateway.delete(gym)
    }
}
