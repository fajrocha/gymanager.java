package com.faroc.gymanager.unit.application.gyms.commands

import com.faroc.gymanager.application.gyms.commands.deletegym.DeleteGymCommand
import com.faroc.gymanager.application.gyms.commands.deletegym.DeleteGymHandler
import com.faroc.gymanager.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.domain.gyms.Gym
import com.faroc.gymanager.domain.gyms.errors.GymsErrors
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException
import com.faroc.gymanager.domain.subscriptions.Subscription
import com.faroc.gymanager.domain.subscriptions.SubscriptionType
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors
import net.datafaker.Faker
import spock.lang.Specification

class DeleteGymHandlerTests extends Specification {
    Faker faker
    DeleteGymHandler sut
    UUID subscriptionId
    String gymName
    UUID adminId
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
        adminId = UUID.randomUUID()
        gymId = UUID.randomUUID()
        anotherGymId = UUID.randomUUID()
        gym = new Gym(
                gymId,
                subscriptionId,
                gymName,
                Integer.MAX_VALUE,
        )

        subscription = Subscription.mapFromStorage(
                subscriptionId,
                adminId,
                SubscriptionType.Free,
                1,
                new UUID[] { gymId }
        )

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
        def subscription = Subscription.mapFromStorage(
                subscriptionId,
                adminId,
                SubscriptionType.Free,
                1,
                new UUID[] { anotherGymId }
        )
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
