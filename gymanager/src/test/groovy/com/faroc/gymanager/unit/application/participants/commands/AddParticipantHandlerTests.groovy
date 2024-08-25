package com.faroc.gymanager.unit.application.participants.commands

import com.faroc.gymanager.sessionmanagement.application.participants.commands.addparticpant.AddParticipantCommand
import com.faroc.gymanager.sessionmanagement.application.participants.commands.addparticpant.AddParticipantHandler
import com.faroc.gymanager.common.application.security.CurrentUserProvider
import com.faroc.gymanager.common.application.security.DTOs.CurrentUserDTO
import com.faroc.gymanager.common.application.security.exceptions.UnauthorizedException
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException
import com.faroc.gymanager.usermanagement.application.users.gateways.UsersGateway
import com.faroc.gymanager.usermanagement.domain.users.User
import com.faroc.gymanager.usermanagement.domain.users.errors.UserErrors
import com.faroc.gymanager.unit.domain.users.utils.UsersTestsFactory
import spock.lang.Specification

class AddParticipantHandlerTests extends Specification {

    UUID userId
    User user
    CurrentUserDTO currentUser
    AddParticipantCommand command

    UsersGateway mockUsersGateway
    CurrentUserProvider mockCurrentUserProvider
    DomainEventsPublisher mockDomainEventsPublisher

    AddParticipantHandler sut


    def setup() {
        userId = UUID.randomUUID()
        user = UsersTestsFactory.create(userId)
        currentUser = new CurrentUserDTO(userId, List.of(), List.of())
        command = new AddParticipantCommand(userId)

        mockUsersGateway = Mock(UsersGateway)
        mockCurrentUserProvider = Mock(CurrentUserProvider)
        mockDomainEventsPublisher = Mock(DomainEventsPublisher)
        sut = new AddParticipantHandler(mockUsersGateway, mockCurrentUserProvider, mockDomainEventsPublisher)
    }

    def "when current user does not match user on request should throw unauthorized exception"() {
        given:
        def currentUser = new CurrentUserDTO(UUID.randomUUID(), List.of(), List.of())
        mockCurrentUserProvider.getCurrentUser() >> currentUser

        when:
        sut.handle(command)

        then:
        thrown(UnauthorizedException)
    }

    def "when user to add participant profile not found should throw unauthorized exception"() {
        given:
        mockCurrentUserProvider.getCurrentUser() >> currentUser
        mockUsersGateway.findById(userId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.message == UserErrors.notFound(userId)
    }

    def "when user exists should add profile and return id"() {
        given:
        mockCurrentUserProvider.getCurrentUser() >> currentUser
        mockUsersGateway.findById(userId) >> Optional.of(user)

        when:
        var adminId = sut.handle(command)

        then:
        adminId == user.getParticipantId()
        1 * mockUsersGateway.update(user)
        1 * mockDomainEventsPublisher.publishEventsFromAggregate(user)
    }
}
