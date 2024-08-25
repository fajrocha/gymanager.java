package com.faroc.gymanager.unit.application.gyms.commands

import com.faroc.gymanager.gymmanagement.application.gyms.commands.addgym.AddGymCommand
import com.faroc.gymanager.gymmanagement.application.gyms.commands.addgym.AddGymHandler
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType
import com.faroc.gymanager.gymmanagement.domain.subscriptions.errors.SubscriptionErrors
import net.datafaker.Faker
import spock.lang.Specification

class AddGymHandlerTests extends Specification {
    Faker faker
    AddGymHandler sut
    UUID subscriptionId
    String gymName
    UUID adminId
    Subscription subscription
    AddGymCommand command

    GymsGateway mockGymsGateway
    SubscriptionsGateway mockSubscriptionsGateway

    def setup() {
        faker = new Faker()
        gymName = faker.company().name()
        subscriptionId = UUID.randomUUID()
        adminId = UUID.randomUUID()
        command = new AddGymCommand(gymName, subscriptionId)
        subscription = Subscription.mapFromStorage(
                subscriptionId,
                adminId,
                SubscriptionType.Free,
                1,
                new UUID[0]
        )

        mockSubscriptionsGateway = Mock(SubscriptionsGateway)
        mockGymsGateway = Mock(GymsGateway)

        sut = new AddGymHandler(mockSubscriptionsGateway, mockGymsGateway)
    }
    

    def "when subscription not found throw not found exception"() {
        given:
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.getDetail() == SubscriptionErrors.NOT_FOUND
    }

    def "when called should creates gym and add to subscription gyms"() {
        given:
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.of(subscription)

        when:
        def gym = sut.handle(command)

        then:
        subscription.hasGym(gym.getId())
        gym.getSubscriptionId() == subscriptionId
        gym.getName() == gymName
        1 * mockGymsGateway.save(_ as Gym)
        1 * mockSubscriptionsGateway.update(subscription)
    }
}
