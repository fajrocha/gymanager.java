package com.faroc.gymanager.unit.application.sessions.commands

import com.faroc.gymanager.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.application.sessions.commands.addsessioncategory.AddSessionCategoriesCommand
import com.faroc.gymanager.application.sessions.commands.addsessioncategory.AddSessionCategoriesHandler
import com.faroc.gymanager.domain.gyms.Gym
import com.faroc.gymanager.domain.sessions.errors.SessionCategoriesErrors
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException
import spock.lang.Specification
import unit.domain.gyms.utils.GymsTestsFactory

class AddSessionCategoriesHandlerTests extends Specification {
    UUID gymId
    Gym gym
    List<String> sessionCategories
    AddSessionCategoriesCommand command
    GymsGateway mockGymsGateway

    AddSessionCategoriesHandler sut

    def setup() {
        gymId = UUID.randomUUID()
        gym = GymsTestsFactory.create(gymId)
        sessionCategories = List.of("Pilates", "Yoga")
        command = new AddSessionCategoriesCommand(gymId, sessionCategories)
        mockGymsGateway = Mock(GymsGateway)

        sut = new AddSessionCategoriesHandler(mockGymsGateway)
    }
    
    def "when adding session categories and gym does not exist should throw unexpected exception"() {
        given:
        mockGymsGateway.findById(gymId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == SessionCategoriesErrors.GYM_NOT_FOUND
        ex.getMessage() == SessionCategoriesErrors.gymNotFound(gymId)
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
