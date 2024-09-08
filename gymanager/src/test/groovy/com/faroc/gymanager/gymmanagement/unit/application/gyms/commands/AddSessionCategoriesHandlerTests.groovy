package com.faroc.gymanager.gymmanagement.unit.application.gyms.commands

import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException
import com.faroc.gymanager.gymmanagement.domain.gyms.errors.GymsErrors
import com.faroc.gymanager.gymmanagement.unit.domain.gyms.utils.GymsTestsFactory
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.gymmanagement.application.gyms.commands.addsessioncategory.AddSessionCategoriesCommand
import com.faroc.gymanager.gymmanagement.application.gyms.commands.addsessioncategory.AddSessionCategoriesHandler
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym
import spock.lang.Specification

class AddSessionCategoriesHandlerTests extends Specification {
    UUID gymId
    UUID subscriptionId
    Gym gym
    List<String> sessionCategories
    AddSessionCategoriesCommand command
    GymsGateway mockGymsGateway

    AddSessionCategoriesHandler sut

    def setup() {
        gymId = UUID.randomUUID()
        subscriptionId = UUID.randomUUID()
        gym = GymsTestsFactory.create(gymId, subscriptionId)
        sessionCategories = List.of("Pilates", "Yoga")
        command = new AddSessionCategoriesCommand(gymId, subscriptionId, sessionCategories)
        mockGymsGateway = Mock(GymsGateway)

        sut = new AddSessionCategoriesHandler(mockGymsGateway)
    }
    
    def "when adding session categories and gym does not exist should throw unexpected exception"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.getDetail() == GymsErrors.NOT_FOUND
        ex.getMessage() == GymsErrors.notFound(gymId, subscriptionId)
    }

    def "when adding session categories and gym's subscription does not match subscription on request should throw unexpected exception"() {
        given:
        def gym = GymsTestsFactory.create(gymId)
        mockGymsGateway.findById(gymId) >> Optional.of(gym)

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == GymsErrors.SUBSCRIPTION_MISMATCH
        ex.getMessage() == GymsErrors.subscriptionMismatch(gymId, subscriptionId, gym.getSubscriptionId())
    }

    def "when adding session categories and gym update fails should rethrow exception"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.of(gym)
        mockGymsGateway.update(gym) >> { throw new RuntimeException() }

        when:
        sut.handle(command)

        then:
        thrown(RuntimeException)
    }

    def "when adding session categories and gym exists should add categories to gym"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.of(gym)

        when:
        def sessionCategories = sut.handle(command)

        then:
        sessionCategories == this.sessionCategories
        gym.getSessionCategories().size() == this.sessionCategories.size()
        1 * mockGymsGateway.update(gym)
    }
}
